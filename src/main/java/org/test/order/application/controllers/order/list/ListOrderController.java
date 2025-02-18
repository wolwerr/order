package org.test.order.application.controllers.order.list;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.test.order.application.controllers.item.list.response.ItemResponse;
import org.test.order.application.controllers.order.list.response.ListAllOrdersResponse;
import org.test.order.application.controllers.order.list.response.OrderResponseList;
import org.test.order.application.response.GenericResponse;
import org.test.order.domain.entity.OrderEntity;
import org.test.order.domain.generic.output.OutputInterface;
import org.test.order.domain.useCase.order.ListAllOrdersUseCase;
import org.test.order.infra.adpter.repository.order.ListOrdersRepository;
import org.test.order.infra.repository.OrderMongoRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class ListOrderController {
    private final OrderMongoRepository orderMongoRepository;

    @GetMapping("/list")
    @Operation(summary = "List all orders", tags = {"Order"})
    public ResponseEntity<Object> getAllOrders() {
        ListAllOrdersUseCase listAllOrdersUseCase = new ListAllOrdersUseCase(new ListOrdersRepository(orderMongoRepository));
        listAllOrdersUseCase.execute();
        OutputInterface outputInterface = listAllOrdersUseCase.getListAllOrdersOutput();

        if (outputInterface.getOutputStatus().getCode() == 200) {
            List<OrderResponseList> orderResponseList = ((List<OrderEntity>) outputInterface.getBody()).stream().map(orderEntity ->
                    new OrderResponseList(
                            orderEntity.getUuid(),
                            orderEntity.getOrderNumber(),
                            orderEntity.getStatusOrder(),
                            orderEntity.getTotalValue(),
                            orderEntity.getCustomerId(),
                            orderEntity.getCreatedAt(),
                            orderEntity.getUpdatedAt(),
                            orderEntity.getItem().stream().map(itemEntity ->
                                    new ItemResponse(
                                            itemEntity.getUuid(),
                                            itemEntity.getName(),
                                            itemEntity.getQuantity(),
                                            itemEntity.getValue(),
                                            itemEntity.getCreatedAt(),
                                            itemEntity.getUpdatedAt()
                                    )
                            ).collect(Collectors.toList())
                    )
            ).collect(Collectors.toList());

            ListAllOrdersResponse response = new ListAllOrdersResponse(orderResponseList);
            return ResponseEntity.status(outputInterface.getOutputStatus().getCode()).body(response);
        }

        return new GenericResponse().response(outputInterface);
    }
}
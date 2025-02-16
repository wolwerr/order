package org.test.order.application.controllers.order.list;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.test.order.application.response.GenericResponse;
import org.test.order.domain.generic.output.OutputInterface;
import org.test.order.domain.useCase.order.ListAllOrdersUseCase;
import org.test.order.infra.adpter.repository.order.ListOrdersRepository;
import org.test.order.infra.repository.OrderMongoRepository;

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
            return new GenericResponse().response(outputInterface);
        }

        return new GenericResponse().response(outputInterface);
    }
}

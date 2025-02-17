package org.test.order.application.controllers.order.createOrder;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.test.order.application.response.GenericResponse;
import org.test.order.domain.generic.output.OutputInterface;
import org.test.order.domain.input.order.CreateOrderInput;
import org.test.order.domain.useCase.order.CreateOrderUseCase;
import org.test.order.infra.adpter.repository.order.CreateOrderRepository;
import org.test.order.infra.collection.item.Item;
import org.test.order.infra.kafka.producers.OrderProducer;
import org.test.order.infra.repository.ItemMongoRepository;
import org.test.order.infra.repository.OrderMongoRepository;
import io.swagger.v3.oas.annotations.Operation;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class CreateOrderController {
    private final OrderMongoRepository orderMongoRepository;
    private final ItemMongoRepository itemMongoRepository;

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String servers;

    @PostMapping
    @Operation(summary = "Create a new order", tags = {"Order"})
    @Transactional
    public ResponseEntity<Object> createOrder(@RequestBody CreateOrderInput createOrderInput) {
        List<Item> items = new ArrayList<>(createOrderInput.items());

        OutputInterface outputInterface = getOutputInterface(createOrderInput, items);
        if (outputInterface.getOutputStatus().getCode() != 201) {
            return new GenericResponse().response(outputInterface);
        }

        return ResponseEntity.status(outputInterface.getOutputStatus().getCode()).body(outputInterface.getBody());
    }

    private OutputInterface getOutputInterface(CreateOrderInput createOrderInput, List<Item> items) {
        CreateOrderInput input = new CreateOrderInput(
                createOrderInput.uuid(),
                createOrderInput.orderNumber(),
                createOrderInput.statusOrder(),
                createOrderInput.totalValue(),
                createOrderInput.customerId(),
                createOrderInput.createdAt(),
                createOrderInput.updatedAt(),
                items
        );
        CreateOrderUseCase useCase = new CreateOrderUseCase(
                new CreateOrderRepository(orderMongoRepository, itemMongoRepository),
                new OrderProducer(servers)
        );
        useCase.execute(input);
        return useCase.getCreateOrderOutput();
    }
}
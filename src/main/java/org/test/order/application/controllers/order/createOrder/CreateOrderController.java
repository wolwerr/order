package org.test.order.application.controllers.order.createOrder;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import org.test.order.infra.kafka.producers.OrderProducer;
import org.test.order.infra.repository.ItemMongoRepository;
import org.test.order.infra.repository.OrderMongoRepository;
import io.swagger.v3.oas.annotations.Operation;



@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class CreateOrderController {
    private final OrderMongoRepository orderMongoRepository;
    private final ItemMongoRepository itemMongoRepository;

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String servers;

    @PostMapping
    @Operation(
            summary = "Create a new order",
            description = "Creates a new order.",
            tags = {"Order"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Order details to be created",
                    required = true
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Order successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @Transactional
    public ResponseEntity<Object> createOrder(@RequestBody CreateOrderInput createOrderInput) {
        OutputInterface outputInterface = getOutputInterface(createOrderInput);
        if (outputInterface.getOutputStatus().getCode() != 201) {
            return new GenericResponse().response(outputInterface);
        }

        return ResponseEntity.status(outputInterface.getOutputStatus().getCode()).body(outputInterface.getBody());
    }

    private OutputInterface getOutputInterface(CreateOrderInput createOrderInput) {
        CreateOrderUseCase useCase = new CreateOrderUseCase(
                new CreateOrderRepository(orderMongoRepository, itemMongoRepository),
                new OrderProducer(servers), itemMongoRepository
        );
        useCase.execute(createOrderInput);
        return useCase.getCreateOrderOutput();
    }
}
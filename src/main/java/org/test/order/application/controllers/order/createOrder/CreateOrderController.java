package org.test.order.application.controllers.order.createOrder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
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


@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class CreateOrderController {
    private final CreateOrderUseCase createOrderUseCase;

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
    public ResponseEntity<Object> createOrder(@RequestBody CreateOrderInput createOrderInput) throws Exception {
        OutputInterface outputInterface = getOutputInterface(createOrderInput);
        if (outputInterface.getOutputStatus().getCode() != 201) {
            return new GenericResponse().response(outputInterface);
        }

        return ResponseEntity.status(outputInterface.getOutputStatus().getCode()).body(outputInterface.getBody());
    }

    private OutputInterface getOutputInterface(CreateOrderInput createOrderInput) throws Exception {
        createOrderUseCase.execute(createOrderInput);
        return createOrderUseCase.getCreateOrderOutput();
    }
}

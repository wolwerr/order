package org.test.order.domain.input.order;

import io.swagger.v3.oas.annotations.media.Schema;
import org.test.order.domain.enuns.StatusOrder;
import org.test.order.infra.collection.item.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Request body for creating a new order")
public record CreateOrderInput(
        @Schema(description = "Unique identifier for the order", example = "123e4567-e89b-12d3-a456-426614174006")
        UUID uuid,

        @Schema(description = "Order number", example = "ORD12345")
        String orderNumber,

        @Schema(description = "Current status of the order", example = "APPROVED")
        StatusOrder statusOrder,

        @Schema(description = "Always will be zero", example = "0.00", required = false)
        Double totalValue,

        @Schema(description = "Customer identifier", example = "223e4567-e89b-12d3-a456-426614174007")
        UUID customerId,

        @Schema(description = "Timestamp when the order was created", example = "2024-02-17T12:00:00")
        LocalDateTime createdAt,

        @Schema(description = "Timestamp when the order was last updated", example = "2024-02-17T12:30:00")
        LocalDateTime updatedAt,

        @Schema(description = "List of items included in the order")
        List<Item> items
) {
}

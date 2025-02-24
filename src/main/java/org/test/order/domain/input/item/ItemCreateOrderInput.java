package org.test.order.domain.input.item;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record ItemCreateOrderInput(

        @Schema(description = "Unique identifier for the order", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID uuid,

        @Schema(description = "Item quantity", example = "1")
        Integer quantity

) {
}
package org.test.order.application.controllers.item.list.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ItemResponse {
    private UUID uuid;
    private String name;
    private int quantity;
    private Double value;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ItemResponse(UUID uuid, String name, int quantity, Double value, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.uuid = uuid;
        this.name = name;
        this.quantity = quantity;
        this.value = value;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

    }
}
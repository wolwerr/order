package org.test.order.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.test.order.domain.exception.item.ItemEmptyException;
import org.test.order.domain.exception.item.ItemValueZeroException;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ItemEntity {
    private UUID uuid;
    private String name;
    private Double value = 0.0;
    private Integer quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ItemEntity() {
    }

    public double verifyValue() throws ItemValueZeroException {
        if (value < 0) {
            throw new ItemValueZeroException("Value must be greater than 0");
        }
        return value;
    }

    public int verifyQuantity() throws ItemEmptyException {
        if (quantity < 0) {
            throw new ItemEmptyException("Quantity must be greater than 0");
        }
        return quantity;
    }

    public ItemEntity(UUID uuid, String name, Double value, Integer quantity, LocalDateTime createdAt, LocalDateTime updatedAt) throws ItemValueZeroException, ItemEmptyException {
        this.uuid = uuid;
        this.name = name;
        this.value = value != null ? value : 0.0;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
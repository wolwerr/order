package org.test.order.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.test.order.domain.exception.item.ItemEmpty;
import org.test.order.domain.exception.item.ItemValueZero;

import java.util.UUID;

@Getter
@Setter
public class ItemEntity {
    private UUID uuid;
    private String name;
    private Double value;
    private Integer quantity;

    public Double getTotalValue() {
        return value * quantity;
    }

    public double verifyValue() {
        if (value < 0) {
            throw new ItemValueZero("Value must be greater than 0");
        }
        return value;
    }

    public int verifyQuantity() {
        if (quantity < 0) {
            throw new ItemEmpty("Quantity must be greater than 0");
        }
        return quantity;
    }

    public ItemEntity(UUID uuid, String name, Double value, Integer quantity) {
        this.uuid = uuid;
        this.name = name;
        this.value = value;
        this.quantity = quantity;
    }

}

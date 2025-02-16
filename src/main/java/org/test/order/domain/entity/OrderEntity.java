package org.test.order.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.test.order.domain.enuns.StatusOrder;
import org.test.order.domain.exception.item.ItemEmpty;
import org.test.order.domain.exception.order.OrderValueZero;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
public class OrderEntity {
    private UUID uuid;
    private String orderNumber;
    private StatusOrder statusOrder;
    private Double totalValue;
    private UUID customerId;
    private List<ItemEntity> item;

    public int verifyItems() {
        if (item.isEmpty()) {
            throw new ItemEmpty("Items must be greater than 0");
        }
        return item.size();
    }

    public  Double verifyTotalValue() {
        if (totalValue < 0) {
            throw new OrderValueZero("Total value must be greater than 0");
        }
        return totalValue;
    }

    public Double totalValue() {
        return item.stream().mapToDouble(ItemEntity::getTotalValue).sum();
    }

}




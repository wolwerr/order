package org.test.order.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.test.order.domain.enuns.StatusOrder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderEntity {
    private UUID uuid;
    private String orderNumber;
    private StatusOrder statusOrder;
    private double totalValue;
    private UUID customerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ItemEntity> item;

    public OrderEntity(UUID uuid, String orderNumber, StatusOrder statusOrder, double totalValue, UUID customerId, LocalDateTime createdAt, LocalDateTime updatedAt, List<ItemEntity> item) {
        this.uuid = UUID.randomUUID();
        this.orderNumber = orderNumber;
        this.statusOrder = statusOrder;
        this.totalValue = totalValue;
        this.customerId = customerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.item = item;
        this.totalValue = calculateTotalValue();
    }

    public OrderEntity() {
    }

    public double calculateTotalValue() {
        return item.stream()
                .mapToDouble(i -> i.getValue() * i.getQuantity())
                .sum();
    }
}
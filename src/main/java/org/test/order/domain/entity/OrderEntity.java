package org.test.order.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.test.order.domain.enuns.StatusOrder;
import org.test.order.domain.exception.item.ItemEmptyException;
import org.test.order.infra.repository.ItemMongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderEntity {
    private UUID uuid;
    private String orderNumber;
    private StatusOrder statusOrder;
    @JsonIgnore
    private Double totalValue = 0.0;
    private UUID customerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ItemEntity> item;

    public OrderEntity(UUID uuid, String orderNumber, StatusOrder statusOrder, Double totalValue, UUID customerId, LocalDateTime createdAt, LocalDateTime updatedAt, List<ItemEntity> item) {
        this.uuid = UUID.randomUUID();
        this.orderNumber = orderNumber;
        this.statusOrder = statusOrder;
        this.totalValue = totalValue != null ? totalValue : 0.0;
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

    public boolean hasSufficientStock(ItemMongoRepository itemMongoRepository) {
        for (ItemEntity item : item) {
            try {
                item.verifyQuantity(); // Verify the quantity of the item
                if (item.getQuantity() > itemMongoRepository.findByUuid(item.getUuid()).orElseThrow().getStock()) {
                    return false;
                }
            } catch (ItemEmptyException e) {
                return false;
            }
        }
        return true;
    }
}
package org.test.order.application.controllers.order.createOrder.response;

import lombok.Getter;
import lombok.Setter;
import org.test.order.application.controllers.item.list.response.ItemResponse;
import org.test.order.domain.enuns.StatusOrder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderResponse {
    private UUID uuid;
    private String orderNumber;
    private StatusOrder statusOrder;
    private UUID customerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ItemResponse> items;

    public OrderResponse(UUID uuid, String orderNumber, StatusOrder statusOrder, Double totalValue, UUID customerId, LocalDateTime createdAt, LocalDateTime updatedAt, List<ItemResponse> items) {
        this.uuid = uuid;
        this.orderNumber = orderNumber;
        this.statusOrder = statusOrder;
        this.customerId = customerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.items = items;
    }

}
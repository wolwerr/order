package org.test.order.application.controllers.order.createOrder.response;

import org.junit.jupiter.api.Test;
import org.test.order.application.controllers.item.list.response.ItemResponse;
import org.test.order.domain.enuns.StatusOrder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderResponseTest {

    @Test
    void testOrderResponseConstructor() {
        UUID uuid = UUID.randomUUID();
        String orderNumber = "12345";
        StatusOrder statusOrder = StatusOrder.PENDING;
        UUID customerId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        List<ItemResponse> items = List.of(new ItemResponse(UUID.randomUUID(), "Item1", 2, 10.0, LocalDateTime.now(), LocalDateTime.now()));

        OrderResponse orderResponse = new OrderResponse(uuid, orderNumber, statusOrder, customerId, createdAt, updatedAt, items);

        assertEquals(uuid, orderResponse.getUuid());
        assertEquals(orderNumber, orderResponse.getOrderNumber());
        assertEquals(statusOrder, orderResponse.getStatusOrder());
        assertEquals(customerId, orderResponse.getCustomerId());
        assertEquals(createdAt, orderResponse.getCreatedAt());
        assertEquals(updatedAt, orderResponse.getUpdatedAt());
        assertEquals(items, orderResponse.getItems());
    }
}
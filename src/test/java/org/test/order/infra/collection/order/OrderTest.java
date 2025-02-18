package org.test.order.infra.collection.order;

import org.junit.jupiter.api.Test;
import org.test.order.domain.enuns.StatusOrder;
import org.test.order.infra.collection.item.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    // Create order with valid UUID, order number, status, value, customer ID and items list
    @Test
    public void create_order_with_valid_parameters() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        String orderNumber = "ORD-001";
        Double totalValue = 100.0;
        LocalDateTime now = LocalDateTime.now();

        Item item = new Item(UUID.randomUUID(), "Test Item", 50.0, 2, now, now);
        List<Item> items = List.of(item);

        Order order = new Order(orderId, orderNumber, StatusOrder.PENDING, totalValue,
                customerId, now, now, items);

        assertNotNull(order);
        assertEquals(orderId, order.getUuid());
        assertEquals(orderNumber, order.getOrderNumber());
        assertEquals(StatusOrder.PENDING, order.getStatusOrder());
        assertEquals(totalValue, order.getTotalValue());
        assertEquals(customerId, order.getCustomerId());
        assertEquals(items, order.getItem());
    }

}
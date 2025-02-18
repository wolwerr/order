package org.test.order.domain.input.order;

import org.junit.jupiter.api.Test;
import org.test.order.domain.enuns.StatusOrder;
import org.test.order.infra.collection.item.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CreateOrderInputTest {

    // Create order with valid UUID, order number, status and items list
    @Test
    public void test_create_order_with_valid_fields() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        List<Item> items = List.of(
                new Item(UUID.randomUUID(), "Item 1", 10.0, 1, now, now)
        );

        CreateOrderInput input = new CreateOrderInput(
                orderId,
                "ORD123",
                StatusOrder.PENDING,
                0.0,
                customerId,
                now,
                now,
                items
        );

        assertNotNull(input);
        assertEquals(orderId, input.uuid());
        assertEquals("ORD123", input.orderNumber());
        assertEquals(StatusOrder.PENDING, input.statusOrder());
        assertEquals(0.0, input.totalValue());
        assertEquals(customerId, input.customerId());
        assertEquals(now, input.createdAt());
        assertEquals(now, input.updatedAt());
        assertEquals(items, input.items());
    }

    // Create order with null UUID
    @Test
    public void test_create_order_with_null_uuid() {
        UUID customerId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        List<Item> items = List.of(
                new Item(UUID.randomUUID(), "Item 1", 10.0, 1, now, now)
        );

        CreateOrderInput input = new CreateOrderInput(
                null,
                "ORD123",
                StatusOrder.PENDING,
                0.0,
                customerId,
                now,
                now,
                items
        );

        assertNotNull(input);
        assertNull(input.uuid());
        assertEquals("ORD123", input.orderNumber());
        assertEquals(StatusOrder.PENDING, input.statusOrder());
        assertEquals(0.0, input.totalValue());
        assertEquals(customerId, input.customerId());
        assertEquals(now, input.createdAt());
        assertEquals(now, input.updatedAt());
        assertEquals(items, input.items());
    }

}
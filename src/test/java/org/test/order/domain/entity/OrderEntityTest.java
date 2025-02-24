package org.test.order.domain.entity;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.test.order.domain.enuns.StatusOrder;
import org.test.order.domain.exception.item.ItemEmptyException;
import org.test.order.domain.exception.item.ItemValueZeroException;
import org.test.order.infra.collection.item.Item;
import org.test.order.infra.repository.ItemMongoRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class OrderEntityTest {

    // Create order with valid items and verify total value calculation
    @Test
    public void calculate_total_value_with_valid_items() throws ItemValueZeroException, ItemEmptyException {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        List<ItemEntity> items = Arrays.asList(
                new ItemEntity(UUID.randomUUID(), "Item 1", 10.0, 2, now, now),
                new ItemEntity(UUID.randomUUID(), "Item 2", 20.0, 1, now, now)
        );

        OrderEntity order = new OrderEntity(
                orderId,
                "ORD-001",
                StatusOrder.PENDING,
                0.0,
                customerId,
                now,
                now,
                items
        );

        double expectedTotal = 40.0;
        assertEquals(expectedTotal, order.getTotalValue(), 0.001);
    }

    // Create new OrderEntity instance with default constructor
    @Test
    public void test_create_order_entity_with_default_constructor() {
        OrderEntity order = new OrderEntity();

        assertNotNull(order);
    }

    // Calculate total value for order with multiple items correctly
    @Test
    public void calculate_total_value_with_multiple_items() throws ItemValueZeroException, ItemEmptyException {
        List<ItemEntity> items = Arrays.asList(
                new ItemEntity(UUID.randomUUID(), "Item1", 10.0, 2, LocalDateTime.now(), LocalDateTime.now()),
                new ItemEntity(UUID.randomUUID(), "Item2", 15.0, 3, LocalDateTime.now(), LocalDateTime.now())
        );

        OrderEntity order = new OrderEntity(
                UUID.randomUUID(),
                "ORDER-001",
                StatusOrder.PENDING,
                0.0,
                UUID.randomUUID(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                items
        );

        double totalValue = order.calculateTotalValue();

        assertEquals(65.0, totalValue);
    }

    // Calculate total value when item list is empty
    @Test
    public void calculate_total_value_with_empty_items() {
        List<ItemEntity> items = new ArrayList<>();

        OrderEntity order = new OrderEntity(
                UUID.randomUUID(),
                "ORDER-001",
                StatusOrder.PENDING,
                0.0,
                UUID.randomUUID(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                items
        );

        double totalValue = order.calculateTotalValue();

        assertEquals(0.0, totalValue);
    }

    @Test
    public void test_hasSufficientStock_returns_false_when_item_out_of_stock() throws ItemValueZeroException, ItemEmptyException {
        // Arrange
        ItemMongoRepository mockItemRepo = Mockito.mock(ItemMongoRepository.class);
        UUID itemId = UUID.randomUUID();
        ItemEntity itemEntity = new ItemEntity(itemId, "Item1", 10.0, 5, null, null);
        List<ItemEntity> itemEntities = List.of(itemEntity);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setItem(itemEntities);

        Item stockItem = new Item();
        stockItem.setUuid(itemId);
        stockItem.setQuantity(3); // Less than required quantity

        when(mockItemRepo.findById(itemId.toString())).thenReturn(Optional.of(stockItem));

        // Act
        boolean result = orderEntity.hasSufficientStock(mockItemRepo);

        // Assert
        assertFalse(result);
    }

}
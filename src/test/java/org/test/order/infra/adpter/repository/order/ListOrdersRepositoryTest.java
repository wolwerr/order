package org.test.order.infra.adpter.repository.order;

import org.junit.jupiter.api.Test;
import org.test.order.domain.entity.OrderEntity;
import org.test.order.domain.enuns.StatusOrder;
import org.test.order.domain.exception.item.ItemEmptyException;
import org.test.order.domain.exception.item.ItemValueZeroException;
import org.test.order.infra.collection.item.Item;
import org.test.order.infra.collection.order.Order;
import org.test.order.infra.repository.OrderMongoRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListOrdersRepositoryTest {

    // Successfully retrieve and convert all orders from MongoDB to OrderEntity list
    @Test
    public void test_list_orders_successfully_converts_orders() throws ItemValueZeroException, ItemEmptyException {
        // Arrange
        OrderMongoRepository mockRepo = mock(OrderMongoRepository.class);
        ListOrdersRepository repository = new ListOrdersRepository(mockRepo);

        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        Item item = new Item(itemId, "Test Item", 10.0, 1, LocalDateTime.now(), LocalDateTime.now());
        List<Item> items = List.of(item);

        Order order = new Order(orderId, "ORDER-001", StatusOrder.PENDING, 10.0, customerId,
                LocalDateTime.now(), LocalDateTime.now(), items);
        when(mockRepo.findAll()).thenReturn(List.of(order));

        // Act
        List<OrderEntity> result = repository.listOrders();

        // Assert
        assertEquals(1, result.size());
        OrderEntity convertedOrder = result.getFirst();
        assertEquals(orderId, convertedOrder.getUuid());
        assertEquals("ORDER-001", convertedOrder.getOrderNumber());
        assertEquals(StatusOrder.PENDING, convertedOrder.getStatusOrder());
        assertEquals(10.0, convertedOrder.getTotalValue());
        assertEquals(customerId, convertedOrder.getCustomerId());
        assertEquals(1, convertedOrder.getItem().size());
        assertEquals(itemId, convertedOrder.getItem().getFirst().getUuid());
    }

    // Handle orders with empty item lists
    @Test
    public void test_list_orders_with_empty_items() throws ItemValueZeroException, ItemEmptyException {
        // Arrange
        OrderMongoRepository mockRepo = mock(OrderMongoRepository.class);
        ListOrdersRepository repository = new ListOrdersRepository(mockRepo);

        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Order order = new Order(orderId, "ORDER-001", StatusOrder.PENDING, 0.0, customerId,
                LocalDateTime.now(), LocalDateTime.now(), new ArrayList<>());
        when(mockRepo.findAll()).thenReturn(List.of(order));

        // Act
        List<OrderEntity> result = repository.listOrders();

        // Assert
        assertEquals(1, result.size());
        OrderEntity convertedOrder = result.getFirst();
        assertEquals(orderId, convertedOrder.getUuid());
        assertTrue(convertedOrder.getItem().isEmpty());
        assertEquals(0.0, convertedOrder.getTotalValue());
    }
}
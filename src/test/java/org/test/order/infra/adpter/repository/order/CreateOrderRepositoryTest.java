package org.test.order.infra.adpter.repository.order;

import org.junit.jupiter.api.Test;
import org.test.order.domain.entity.ItemEntity;
import org.test.order.domain.entity.OrderEntity;
import org.test.order.domain.enuns.StatusOrder;
import org.test.order.domain.exception.item.ItemEmptyException;
import org.test.order.domain.exception.item.ItemValueZeroException;
import org.test.order.domain.exception.order.OrderValueZeroException;
import org.test.order.infra.collection.item.Item;
import org.test.order.infra.collection.order.Order;
import org.test.order.infra.repository.ItemMongoRepository;
import org.test.order.infra.repository.OrderMongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateOrderRepositoryTest {

    // Successfully save new order with valid items and update item quantities
    @Test
    public void test_save_order_with_valid_items_success() throws ItemValueZeroException, ItemEmptyException,  OrderValueZeroException {
        // Arrange
        OrderMongoRepository orderMongoRepository = mock(OrderMongoRepository.class);
        ItemMongoRepository itemMongoRepository = mock(ItemMongoRepository.class);
        CreateOrderRepository createOrderRepository = new CreateOrderRepository(orderMongoRepository, itemMongoRepository);

        UUID itemUuid = UUID.randomUUID();
        Item existingItem = new Item(itemUuid, "Test Item", 10.0, 5, LocalDateTime.now(), LocalDateTime.now());
        when(itemMongoRepository.findByUuid(itemUuid)).thenReturn(Optional.of(existingItem));

        ItemEntity itemEntity = new ItemEntity(itemUuid, "Test Item", 10.0, 2, LocalDateTime.now(), LocalDateTime.now());
        List<ItemEntity> items = List.of(itemEntity);

        OrderEntity orderEntity = new OrderEntity(
                UUID.randomUUID(),
                "ORDER123",
                StatusOrder.PENDING,
                20.0,
                UUID.randomUUID(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                items
        );

        Order savedOrder = new Order(
                orderEntity.getUuid(),
                orderEntity.getOrderNumber(),
                orderEntity.getStatusOrder(),
                orderEntity.getTotalValue(),
                orderEntity.getCustomerId(),
                orderEntity.getCreatedAt(),
                orderEntity.getUpdatedAt(),
                List.of(existingItem)
        );
        when(orderMongoRepository.save(any(Order.class))).thenReturn(savedOrder);

        // Act
        createOrderRepository.saveOrder(orderEntity);

        // Assert
        verify(itemMongoRepository).save(argThat(item -> item.getQuantity() == 3));
        verify(orderMongoRepository).save(any(Order.class));
    }

    // Handle attempt to save order with non-existent items
    @Test
    public void test_save_order_with_nonexistent_item_throws_exception() throws ItemValueZeroException, ItemEmptyException {
        // Arrange
        OrderMongoRepository orderMongoRepository = mock(OrderMongoRepository.class);
        ItemMongoRepository itemMongoRepository = mock(ItemMongoRepository.class);
        CreateOrderRepository createOrderRepository = new CreateOrderRepository(orderMongoRepository, itemMongoRepository);

        UUID nonExistentItemUuid = UUID.randomUUID();
        when(itemMongoRepository.findByUuid(nonExistentItemUuid)).thenReturn(Optional.empty());

        ItemEntity itemEntity = new ItemEntity(nonExistentItemUuid, "Test Item", 10.0, 2, LocalDateTime.now(), LocalDateTime.now());
        List<ItemEntity> items = List.of(itemEntity);

        OrderEntity orderEntity = new OrderEntity(
                UUID.randomUUID(),
                "ORDER123",
                StatusOrder.PENDING,
                20.0,
                UUID.randomUUID(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                items
        );

        // Act & Assert
        assertThrows(RuntimeException.class, () -> createOrderRepository.saveOrder(orderEntity));
        verify(orderMongoRepository, never()).save(any(Order.class));
    }


}
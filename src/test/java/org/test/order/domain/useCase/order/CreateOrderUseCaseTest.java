package org.test.order.domain.useCase.order;

import org.junit.jupiter.api.Test;
import org.test.order.domain.entity.OrderEntity;
import org.test.order.domain.enuns.StatusOrder;
import org.test.order.domain.exception.order.OrderValueZeroException;
import org.test.order.domain.generic.output.OutputError;
import org.test.order.domain.input.order.CreateOrderInput;
import org.test.order.domain.output.order.CreateOrderOutput;
import org.test.order.infra.adpter.repository.order.CreateOrderRepository;
import org.test.order.infra.collection.item.Item;
import org.test.order.infra.kafka.producers.OrderProducer;
import org.test.order.infra.repository.ItemMongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateOrderUseCaseTest {

    // Successfully creates order with valid items and sufficient stock
    @Test
    public void test_create_order_with_valid_items_and_stock() throws OrderValueZeroException {
        // Arrange
        CreateOrderRepository mockRepository = mock(CreateOrderRepository.class);
        ItemMongoRepository mockItemRepo = mock(ItemMongoRepository.class);
        OrderProducer mockProducer = mock(OrderProducer.class);
        CreateOrderUseCase useCase = new CreateOrderUseCase(mockRepository, mockProducer, mockItemRepo);

        UUID itemId = UUID.randomUUID();
        Item item = new Item(itemId, "Test Item", 10.0, 2, LocalDateTime.now(), LocalDateTime.now());
        List<Item> items = List.of(item);

        CreateOrderInput input = new CreateOrderInput(
                UUID.randomUUID(),
                "ORD123",
                StatusOrder.PENDING,
                0.0,
                UUID.randomUUID(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                items
        );

        when(mockItemRepo.findById(itemId.toString()))
                .thenReturn(Optional.of(new Item(itemId, "Test Item", 10.0, 5, LocalDateTime.now(), LocalDateTime.now())));

        // Act
        useCase.execute(input);

        // Assert
        verify(mockRepository).saveOrder(any(OrderEntity.class));
        verify(mockProducer).sendOrder(any(CreateOrderOutput.class));
        assertTrue(useCase.getCreateOrderOutput() instanceof CreateOrderOutput);
        assertEquals(201, ((CreateOrderOutput)useCase.getCreateOrderOutput()).getOutputStatus().getCode());
    }

    // Handles items with zero or negative quantities
    @Test
    public void test_create_order_with_invalid_quantity() throws OrderValueZeroException {
        // Arrange
        CreateOrderRepository mockRepository = mock(CreateOrderRepository.class);
        ItemMongoRepository mockItemRepo = mock(ItemMongoRepository.class);
        OrderProducer mockProducer = mock(OrderProducer.class);
        CreateOrderUseCase useCase = new CreateOrderUseCase(mockRepository, mockProducer, mockItemRepo);

        UUID itemId = UUID.randomUUID();
        Item item = new Item(itemId, "Test Item", 10.0, -1, LocalDateTime.now(), LocalDateTime.now());
        List<Item> items = List.of(item);

        CreateOrderInput input = new CreateOrderInput(
                UUID.randomUUID(),
                "ORD123",
                StatusOrder.PENDING,
                0.0,
                UUID.randomUUID(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                items
        );

        // Act
        useCase.execute(input);

        // Assert
        verify(mockRepository, never()).saveOrder(any(OrderEntity.class));
        assertTrue(useCase.getCreateOrderOutput() instanceof OutputError);
        assertEquals(500, ((OutputError)useCase.getCreateOrderOutput()).getOutputStatus().getCode());
    }

}
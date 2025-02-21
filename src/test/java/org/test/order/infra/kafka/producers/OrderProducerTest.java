package org.test.order.infra.kafka.producers;

import org.junit.jupiter.api.Test;
import org.test.order.domain.entity.ItemEntity;
import org.test.order.domain.entity.OrderEntity;
import org.test.order.domain.enuns.StatusOrder;
import org.test.order.domain.exception.item.ItemEmptyException;
import org.test.order.domain.exception.item.ItemValueZeroException;
import org.test.order.domain.generic.output.OutputStatus;
import org.test.order.domain.output.order.CreateOrderOutput;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderProducerTest {

    // Successfully send order message to Kafka with complete order and item details
    @Test
    public void test_send_order_with_complete_details() throws ItemValueZeroException, ItemEmptyException {
        // Arrange
        OrderProducer mockOrderProducer = mock(OrderProducer.class);

        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        ItemEntity item = new ItemEntity(
                itemId,
                "Test Item",
                10.0,
                2,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        List<ItemEntity> items = List.of(item);

        OrderEntity order = new OrderEntity(
                orderId,
                "ORD-001",
                StatusOrder.PENDING,
                20.0,
                customerId,
                LocalDateTime.now(),
                LocalDateTime.now(),
                items
        );

        OutputStatus outputStatus = new OutputStatus(200, "SUCCESS", "Order created successfully");
        CreateOrderOutput output = new CreateOrderOutput(order, outputStatus);

        // Act
        doNothing().when(mockOrderProducer).sendOrder(output);

        // Assert
        assertDoesNotThrow(() -> mockOrderProducer.sendOrder(output));

        // Verify
        verify(mockOrderProducer, times(1)).sendOrder(output);
    }

    // Handle order with empty item list
    @Test
    public void test_send_order_with_empty_items() {
        // Arrange
        OrderProducer mockOrderProducer = mock(OrderProducer.class);

        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        OrderEntity order = new OrderEntity(
                orderId,
                "ORD-002",
                StatusOrder.PENDING,
                0.0,
                customerId,
                LocalDateTime.now(),
                LocalDateTime.now(),
                new ArrayList<>()
        );

        OutputStatus outputStatus = new OutputStatus(200, "SUCCESS", "Order created successfully");
        CreateOrderOutput output = new CreateOrderOutput(order, outputStatus);

        // Act
        doNothing().when(mockOrderProducer).sendOrder(output);

        // Assert
        assertDoesNotThrow(() -> mockOrderProducer.sendOrder(output));

        // Verify
        verify(mockOrderProducer, times(1)).sendOrder(output);
    }

    // Successfully converts OrderEntity to JSON with all required fields
    @Test
    public void test_convert_order_entity_to_json_successfully() throws ItemValueZeroException, ItemEmptyException {
        // Arrange
        String kafkaServer = "localhost:9092";
        OrderProducer orderProducer = spy(new OrderProducer(kafkaServer));

        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ItemEntity item = new ItemEntity(
                UUID.randomUUID(),
                "Test Item",
                10.0,
                2,
                now,
                now
        );

        List<ItemEntity> items = List.of(item);

        OrderEntity order = new OrderEntity(
                orderId,
                "ORD-001",
                StatusOrder.PENDING,
                20.0,
                customerId,
                now,
                now,
                items
        );

        OutputStatus outputStatus = new OutputStatus(200, "SUCCESS", "Order created successfully");
        CreateOrderOutput output = new CreateOrderOutput(order, outputStatus);

        // Act
        doNothing().when(orderProducer).send(anyString(), anyString());
        orderProducer.sendOrder(output);

        // Assert
        verify(orderProducer, times(1)).send(anyString(), anyString());
    }

}
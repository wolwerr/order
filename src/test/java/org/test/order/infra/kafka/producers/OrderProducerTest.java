package org.test.order.infra.kafka.producers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;
import org.test.order.domain.entity.ItemEntity;
import org.test.order.domain.entity.OrderEntity;
import org.test.order.domain.enuns.StatusOrder;
import org.test.order.domain.generic.output.OutputStatus;
import org.test.order.domain.output.order.CreateOrderOutput;
import org.test.order.infra.collection.Fallback.FallbackOrderEntity;
import org.test.order.infra.repository.FallbackOrderRepository;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderProducerTest {

    private KafkaTemplate<String, String> kafkaTemplate;
    private FallbackOrderRepository fallbackOrderRepository;
    private OrderProducer orderProducer;

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        fallbackOrderRepository = mock(FallbackOrderRepository.class);
        orderProducer = new OrderProducer(kafkaTemplate, fallbackOrderRepository);
    }

    @Test
    void shouldSendOrderSuccessfully() throws Exception {
        // Arrange
        OrderEntity orderEntity = createOrderEntity();
        OutputStatus outputStatus = new OutputStatus(200, "SUCCESS", "Order created");
        CreateOrderOutput createOrderOutput = new CreateOrderOutput(orderEntity, outputStatus);

        // Act
        orderProducer.sendOrder(createOrderOutput);

        // Assert
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(kafkaTemplate, times(1)).send(anyString(), anyString(), messageCaptor.capture());

        String jsonMessage = messageCaptor.getValue();
        assertNotNull(jsonMessage);
        assertTrue(jsonMessage.contains(orderEntity.getUuid().toString()), "JSON should contain order UUID");
    }

    @Test
    void shouldFallbackWhenKafkaFails() {
        // Arrange
        OrderEntity orderEntity = createOrderEntity();
        OutputStatus outputStatus = new OutputStatus(200, "SUCCESS", "Order created");
        CreateOrderOutput createOrderOutput = new CreateOrderOutput(orderEntity, outputStatus);

        doThrow(new RuntimeException("Kafka error")).when(kafkaTemplate).send(anyString(), anyString(), anyString());

        // Act
        orderProducer.fallbackSendOrder(createOrderOutput, new RuntimeException("Kafka error"));

        // Assert
        verify(fallbackOrderRepository, times(1)).save(any(FallbackOrderEntity.class));
    }

    @Test
    void shouldConvertToFallbackCorrectly() {
        // Arrange
        OrderEntity orderEntity = createOrderEntity();

        // Act
        FallbackOrderEntity fallbackOrder = orderProducer.convertToFallback(orderEntity);

        // Assert
        assertNotNull(fallbackOrder);
        assertEquals(orderEntity.getUuid(), fallbackOrder.getId());
        assertEquals(orderEntity.getOrderNumber(), fallbackOrder.getOrderNumber());
        assertEquals(orderEntity.getStatusOrder(), fallbackOrder.getStatusOrder());
        assertEquals(orderEntity.getTotalValue(), fallbackOrder.getTotalValue());
    }

    private OrderEntity createOrderEntity() {
        OrderEntity order = new OrderEntity();
        order.setUuid(UUID.randomUUID());
        order.setOrderNumber("12345");
        order.setStatusOrder(StatusOrder.valueOf("APPROVED"));
        order.setTotalValue(150.00);
        order.setCustomerId(UUID.randomUUID());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setItem(Collections.singletonList(createItemEntity()));

        return order;
    }

    private ItemEntity createItemEntity() {
        ItemEntity item = new ItemEntity();
        item.setUuid(UUID.randomUUID());
        item.setName("Product Test");
        item.setValue(50.00);
        item.setQuantity(2);
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());

        return item;
    }
}

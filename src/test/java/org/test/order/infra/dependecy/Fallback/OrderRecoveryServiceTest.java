package org.test.order.infra.dependecy.Fallback;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.test.order.domain.output.order.CreateOrderOutput;
import org.test.order.infra.collection.Fallback.FallbackEnum;
import org.test.order.infra.collection.Fallback.FallbackOrderEntity;
import org.test.order.infra.kafka.producers.OrderProducer;
import org.test.order.infra.repository.FallbackOrderRepository;

import java.util.Collections;
import java.util.List;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderRecoveryServiceTest {

    private final FallbackOrderRepository fallbackOrderRepository = mock(FallbackOrderRepository.class);
    private final OrderProducer orderProducer = mock(OrderProducer.class);
    private final OrderRecoveryService orderRecoveryService = new OrderRecoveryService(fallbackOrderRepository, orderProducer);

    // Service successfully retrieves and processes failed orders from repository
    @Test
    public void test_successful_order_reprocessing() {
        // Given
        FallbackOrderEntity fallbackOrder = new FallbackOrderEntity();
        fallbackOrder.setId(UUID.randomUUID());
        fallbackOrder.setFallbackStatus(FallbackEnum.FAILED);
        fallbackOrder.setCustomerId(UUID.randomUUID().toString());
        List<FallbackOrderEntity> failedOrders = List.of(fallbackOrder);

        when(fallbackOrderRepository.findByFallbackStatus(FallbackEnum.FAILED)).thenReturn(failedOrders);
        doNothing().when(orderProducer).sendOrder(any(CreateOrderOutput.class));

        // When
        orderRecoveryService.reprocessFailedOrders();

        // Then
        verify(fallbackOrderRepository).findByFallbackStatus(FallbackEnum.FAILED);
        verify(fallbackOrderRepository).delete(fallbackOrder);
        verify(orderProducer).sendOrder(any(CreateOrderOutput.class));
    }

    // Order status changes from FAILED to PENDING during processing
    @Test
    public void test_order_status_changes_during_processing() {
        // Given
        FallbackOrderEntity fallbackOrder = new FallbackOrderEntity();
        fallbackOrder.setId(UUID.randomUUID());
        fallbackOrder.setFallbackStatus(FallbackEnum.FAILED);
        List<FallbackOrderEntity> failedOrders = List.of(fallbackOrder);

        when(fallbackOrderRepository.findByFallbackStatus(FallbackEnum.FAILED)).thenReturn(failedOrders);

        // When
        orderRecoveryService.reprocessFailedOrders();

        // Then
        ArgumentCaptor<FallbackOrderEntity> orderCaptor = ArgumentCaptor.forClass(FallbackOrderEntity.class);
        verify(fallbackOrderRepository).save(orderCaptor.capture());
        assertEquals(FallbackEnum.PENDING, orderCaptor.getValue().getFallbackStatus());
    }

    // Empty list of failed orders
    @Test
    public void test_empty_failed_orders_list() {
        // Given
        when(fallbackOrderRepository.findByFallbackStatus(FallbackEnum.FAILED)).thenReturn(Collections.emptyList());

        // When
        orderRecoveryService.reprocessFailedOrders();

        // Then
        verify(fallbackOrderRepository).findByFallbackStatus(FallbackEnum.FAILED);
        verify(orderProducer, never()).sendOrder(any(CreateOrderOutput.class));
        verify(fallbackOrderRepository, never()).delete(any(FallbackOrderEntity.class));
    }

    // Null customerId in fallback order
    @Test
    public void test_null_customer_id_handling() {
        // Given
        FallbackOrderEntity fallbackOrder = new FallbackOrderEntity();
        fallbackOrder.setId(UUID.randomUUID());
        fallbackOrder.setFallbackStatus(FallbackEnum.FAILED);
        fallbackOrder.setCustomerId(null);
        List<FallbackOrderEntity> failedOrders = List.of(fallbackOrder);

        when(fallbackOrderRepository.findByFallbackStatus(FallbackEnum.FAILED)).thenReturn(failedOrders);

        // When
        orderRecoveryService.reprocessFailedOrders();

        // Then
        ArgumentCaptor<CreateOrderOutput> outputCaptor = ArgumentCaptor.forClass(CreateOrderOutput.class);
        verify(orderProducer).sendOrder(outputCaptor.capture());
        assertNull(outputCaptor.getValue().getOrderEntity().getCustomerId());
    }

    @Test
    public void test_reprocessFailedOrders_handlesException() {
        // Given
        FallbackOrderEntity fallbackOrder = new FallbackOrderEntity();
        fallbackOrder.setId(UUID.randomUUID());
        fallbackOrder.setFallbackStatus(FallbackEnum.FAILED);
        List<FallbackOrderEntity> failedOrders = List.of(fallbackOrder);

        when(fallbackOrderRepository.findByFallbackStatus(FallbackEnum.FAILED)).thenReturn(failedOrders);
        doThrow(new RuntimeException("Simulated exception")).when(orderProducer).sendOrder(any());

        // When
        orderRecoveryService.reprocessFailedOrders();

        // Then
        verify(fallbackOrderRepository).findByFallbackStatus(FallbackEnum.FAILED);
        verify(orderProducer).sendOrder(any());
        verify(fallbackOrderRepository, times(2)).save(fallbackOrder);
        assertEquals(FallbackEnum.FAILED, fallbackOrder.getFallbackStatus());
    }

}
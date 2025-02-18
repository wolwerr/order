package org.test.order.application.controllers.order.createOrder;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.test.order.domain.enuns.StatusOrder;
import org.test.order.domain.generic.output.OutputInterface;
import org.test.order.domain.generic.output.OutputStatus;
import org.test.order.domain.input.order.CreateOrderInput;
import org.test.order.infra.collection.item.Item;
import org.test.order.infra.collection.order.Order;
import org.test.order.infra.repository.ItemMongoRepository;
import org.test.order.infra.repository.OrderMongoRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreateOrderControllerTest {

    @Test
    public void test_create_order_with_minimum_required_fields() {
        // Arrange
        OrderMongoRepository orderMongoRepository = mock(OrderMongoRepository.class);
        ItemMongoRepository itemMongoRepository = mock(ItemMongoRepository.class);

        CreateOrderController controller = new CreateOrderController(orderMongoRepository, itemMongoRepository);
        ReflectionTestUtils.setField(controller, "servers", "localhost:9092");

        List<Item> items = new ArrayList<>();
        UUID orderId = UUID.fromString("123e4567-e89b-12d3-a456-426614174006");
        UUID userId = UUID.fromString("223e4567-e89b-12d3-a456-426614174007");

        CreateOrderInput input = new CreateOrderInput(
                orderId, "ORD123", StatusOrder.APPROVED, 0.0, userId, LocalDateTime.now(), LocalDateTime.now(), items
        );

        when(orderMongoRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));


        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        // Act
        ResponseEntity<Object> response = controller.createOrder(input);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());


        verify(orderMongoRepository, times(1)).save(orderCaptor.capture());


        Order savedOrder = orderCaptor.getValue();

        assertNotNull(savedOrder);
        assertEquals("ORD123", savedOrder.getOrderNumber());
        assertEquals(StatusOrder.APPROVED, savedOrder.getStatusOrder());
        assertEquals(0.0, savedOrder.getTotalValue());
        assertEquals(0, savedOrder.getItem().size());
    }

    @Test
    public void test_createOrder_returnsGenericResponse() {
        // Arrange
        OrderMongoRepository orderMongoRepository = mock(OrderMongoRepository.class);
        ItemMongoRepository itemMongoRepository = mock(ItemMongoRepository.class);
        CreateOrderController controller = new CreateOrderController(orderMongoRepository, itemMongoRepository);
        ReflectionTestUtils.setField(controller, "servers", "localhost:9092");

        List<Item> items = new ArrayList<>();
        UUID orderId = UUID.fromString("123e4567-e89b-12d3-a456-426614174006");
        UUID userId = UUID.fromString("223e4567-e89b-12d3-a456-426614174007");

        CreateOrderInput input = new CreateOrderInput(
                orderId, "ORD123", StatusOrder.APPROVED, 0.0, userId, LocalDateTime.now(), LocalDateTime.now(), items
        );

        OutputInterface outputInterface = mock(OutputInterface.class);
        OutputStatus outputStatus = mock(OutputStatus.class);
        when(outputInterface.getOutputStatus()).thenReturn(outputStatus);
        when(outputStatus.getCode()).thenReturn(201);
        when(outputInterface.getBody()).thenReturn("Order Created");

        Order savedOrder = new Order(
                orderId,
                "ORD123",
                StatusOrder.APPROVED,
                0.0,
                userId,
                LocalDateTime.now(),
                LocalDateTime.now(),
                items
        );
        when(orderMongoRepository.save(any(Order.class))).thenReturn(savedOrder);

        // Act
        ResponseEntity<Object> response = controller.createOrder(input);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}

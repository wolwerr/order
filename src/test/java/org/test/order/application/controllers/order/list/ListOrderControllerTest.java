package org.test.order.application.controllers.order.list;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.test.order.domain.entity.OrderEntity;
import org.test.order.domain.enuns.StatusOrder;
import org.test.order.infra.collection.order.Order;
import org.test.order.infra.repository.OrderMongoRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListOrderControllerTest {

    // Successfully retrieve and return list of all orders with 200 status code
    @Test
    public void test_get_all_orders_returns_200_with_order_list() {
        // Arrange
        OrderMongoRepository mockRepo = mock(OrderMongoRepository.class);
        List<Order> orders = createTestOrders();
        when(mockRepo.findAll()).thenReturn(orders);

        ListOrderController controller = new ListOrderController(mockRepo);

        // Act
        ResponseEntity<Object> response = controller.getAllOrders();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(List.class, response.getBody());
        List<OrderEntity> responseBody = (List<OrderEntity>) response.getBody();
        assertEquals(2, responseBody.size());

        OrderEntity firstOrder = responseBody.getFirst();
        assertEquals("ORD-001", firstOrder.getOrderNumber());
        assertEquals("PENDING", firstOrder.getStatusOrder().name());
        assertEquals(100.0, firstOrder.getTotalValue());

        OrderEntity secondOrder = responseBody.get(1);
        assertEquals("ORD-002", secondOrder.getOrderNumber());
        assertEquals("APPROVED", secondOrder.getStatusOrder().name());
        assertEquals(200.0, secondOrder.getTotalValue());

        verify(mockRepo).findAll();
    }

    private List<Order> createTestOrders() {
        Order order1 = new Order();
        order1.setUuid(UUID.randomUUID());
        order1.setOrderNumber("ORD-001");
        order1.setStatusOrder(StatusOrder.valueOf("PENDING"));
        order1.setTotalValue(100.0);
        order1.setItem(new ArrayList<>());
        order1.setCreatedAt(LocalDateTime.now());
        order1.setUpdatedAt(LocalDateTime.now());

        Order order2 = new Order();
        order2.setUuid(UUID.randomUUID());
        order2.setOrderNumber("ORD-002");
        order2.setStatusOrder(StatusOrder.valueOf("APPROVED"));
        order2.setTotalValue(200.0);
        order2.setItem(new ArrayList<>());
        order2.setCreatedAt(LocalDateTime.now());
        order2.setUpdatedAt(LocalDateTime.now());

        return Arrays.asList(order1, order2);
    }

    // Handle null response from ListOrdersRepository
    @Test
    public void test_get_all_orders_returns_500_when_repository_returns_null() {
        // Arrange
        OrderMongoRepository mockRepo = mock(OrderMongoRepository.class);
        when(mockRepo.findAll()).thenReturn(null);

        ListOrderController controller = new ListOrderController(mockRepo);

        // Act
        ResponseEntity<Object> response = controller.getAllOrders();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(mockRepo).findAll();
    }

}
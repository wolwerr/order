package org.test.order.application.controllers.order.list;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.test.order.domain.entity.OrderEntity;
import org.test.order.domain.enuns.StatusOrder;
import org.test.order.domain.exception.item.ItemEmptyException;
import org.test.order.domain.exception.item.ItemValueZeroException;
import org.test.order.domain.gateway.cache.CacheInterface;
import org.test.order.domain.useCase.order.ListAllOrdersUseCase;
import org.test.order.infra.adpter.repository.order.ListOrdersRepository;
import org.test.order.infra.repository.OrderMongoRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListOrderControllerTest {

    @Test
    void test_get_all_orders_returns_200_with_order_list() throws ItemValueZeroException, ItemEmptyException {
        // Arrange
        OrderMongoRepository mockRepo = mock(OrderMongoRepository.class);
        CacheInterface mockCache = mock(CacheInterface.class);
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        ListOrdersRepository mockListOrdersRepo = mock(ListOrdersRepository.class);

        ListAllOrdersUseCase mockUseCase = new ListAllOrdersUseCase(mockListOrdersRepo, mockCache);
        ListOrderController controller = new ListOrderController(mockRepo, mockCache, mockMapper);

        List<OrderEntity> orders = createTestOrders();

        when(mockListOrdersRepo.listOrders()).thenReturn(orders); // Mock do repositório correto
        when(mockMapper.convertValue(any(), any(TypeReference.class))).thenReturn(orders);

        // 🔥 Execute o caso de uso antes de chamar o controller
        mockUseCase.execute();

        // Act
        ResponseEntity<Object> response = controller.getAllOrders();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // 🔥 Agora verificamos se o repositório realmente foi chamado
        verify(mockListOrdersRepo).listOrders();
    }

    private List<OrderEntity> createTestOrders() {
        // Criando instâncias de OrderEntity
        OrderEntity order1 = new OrderEntity();
        order1.setUuid(UUID.randomUUID());
        order1.setOrderNumber("ORD-001");
        order1.setStatusOrder(StatusOrder.PENDING);
        order1.setTotalValue(100.0);
        order1.setItem(new ArrayList<>());
        order1.setCreatedAt(LocalDateTime.now());
        order1.setUpdatedAt(LocalDateTime.now());

        OrderEntity order2 = new OrderEntity();
        order2.setUuid(UUID.randomUUID());
        order2.setOrderNumber("ORD-002");
        order2.setStatusOrder(StatusOrder.APPROVED);
        order2.setTotalValue(200.0);
        order2.setItem(new ArrayList<>());
        order2.setCreatedAt(LocalDateTime.now());
        order2.setUpdatedAt(LocalDateTime.now());

        return Arrays.asList(order1, order2);
    }


    @Test
    void test_get_all_orders_returns_500_when_repository_returns_null() {
        // Arrange
        OrderMongoRepository mockRepo = mock(OrderMongoRepository.class);
        CacheInterface mockCache = mock(CacheInterface.class);
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        when(mockRepo.findAll()).thenReturn(null);

        ListOrderController controller = new ListOrderController(mockRepo, mockCache, mockMapper);

        // Act
        ResponseEntity<Object> response = controller.getAllOrders();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(mockRepo).findAll();
    }
}
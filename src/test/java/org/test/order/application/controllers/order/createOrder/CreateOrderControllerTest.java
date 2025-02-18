//package org.test.order.application.controllers.order.createOrder;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.test.order.domain.enuns.StatusOrder;
//import org.test.order.domain.generic.output.OutputInterface;
//import org.test.order.domain.generic.output.OutputStatus;
//import org.test.order.domain.input.order.CreateOrderInput;
//import org.test.order.infra.collection.item.Item;
//import org.test.order.infra.collection.order.Order;
//import org.test.order.infra.repository.ItemMongoRepository;
//import org.test.order.infra.repository.OrderMongoRepository;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//public class CreateOrderControllerTest {
//
//    @Mock
//    private OrderMongoRepository orderMongoRepository;
//
//    @Mock
//    private ItemMongoRepository itemMongoRepository;
//
//    @Mock
//    private OutputInterface outputInterface;
//
//    @Mock
//    private OutputStatus outputStatus;
//
//    @InjectMocks
//    private CreateOrderController controller;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        ReflectionTestUtils.setField(controller, "servers", "localhost:9092");
//    }
//
//    @Test
//    public void test_createOrder_returnsGenericResponse() {
//        // Arrange
//        List<Item> items = new ArrayList<>();
//        UUID itemId = UUID.fromString("323e4567-e89b-12d3-a456-426614174008");
//        Item item = new Item(itemId, "Item1", 5.0, 2, LocalDateTime.now(), LocalDateTime.now());
//        items.add(item);
//
//        UUID orderId = UUID.randomUUID();
//        UUID userId = UUID.randomUUID();
//
//        CreateOrderInput input = new CreateOrderInput(
//                orderId, "ORD123", StatusOrder.APPROVED, 10.0, userId, LocalDateTime.now(), LocalDateTime.now(), items
//        );
//
//        // Configuração do mock para OutputInterface
//        when(outputInterface.getOutputStatus()).thenReturn(outputStatus);
//        when(outputStatus.getCode()).thenReturn(201);
//        when(outputInterface.getBody()).thenReturn("Order Created");
//
//        // Configuração do mock para salvar no repositório
//        Order savedOrder = new Order(
//                orderId, "ORD123", StatusOrder.APPROVED, 10.0, userId, LocalDateTime.now(), LocalDateTime.now(), items
//        );
//        when(orderMongoRepository.save(any(Order.class))).thenReturn(savedOrder);
//        when(itemMongoRepository.save(any(Item.class))).thenReturn(item);
//
//        // Act
//        ResponseEntity<Object> response = controller.createOrder(input);
//
//        // Assert
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertEquals("Order Created", response.getBody()); // Garante que a resposta contém o corpo esperado
//
//        // Verifica se o repositório foi chamado corretamente
//        verify(orderMongoRepository, times(1)).save(any(Order.class));
//        verify(itemMongoRepository, times(1)).save(any(Item.class));
//    }
//}
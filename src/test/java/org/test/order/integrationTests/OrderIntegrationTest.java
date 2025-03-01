package org.test.order.integrationTests;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.test.order.domain.input.item.ItemCreateOrderInput;
import org.test.order.domain.input.order.CreateOrderInput;


import org.test.order.infra.collection.item.Item;

import org.test.order.domain.enuns.StatusOrder;
import org.test.order.infra.collection.order.Order;
import org.test.order.infra.repository.ItemMongoRepository;
import org.test.order.infra.repository.OrderMongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled("Integration tests disabled until the database is available")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ItemMongoRepository itemMongoRepository;

    @Autowired
    private OrderMongoRepository orderRepository;

    private CreateOrderInput createOrderInput;
    private UUID itemUuid;
    @Autowired
    private OrderMongoRepository orderMongoRepository;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @BeforeEach
    void setUp() {

        itemMongoRepository.deleteAll();
        orderRepository.deleteAll();

        itemUuid = UUID.fromString(UUID.randomUUID().toString());


        Item sampleItem = new Item(itemUuid, "Test Item", 10.0, 5, LocalDateTime.now(), LocalDateTime.now());
        itemMongoRepository.save(sampleItem);

        createOrderInput = new CreateOrderInput(
                UUID.randomUUID(),
                "ORD12345",
                StatusOrder.PENDING,
                UUID.randomUUID(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(new ItemCreateOrderInput(itemUuid, 1))
        );
    }

    @Test
    public void testCreateOrder() throws Exception {

        String authToken = secretKey;

        String responseToken = mockMvc.perform(post("/auth/token")
                        .header("Authorization", "Bearer " + authToken)
                        .header("accept", "*/*")
                        .content(""))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String orderJson = objectMapper.writeValueAsString(createOrderInput);

        mockMvc.perform(post("/orders")
                        .header("Authorization", "Bearer " + responseToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isCreated());

        Optional<Order> savedOrder = Optional.ofNullable(orderRepository.findByOrderNumber("ORD12345"));
        assertTrue(savedOrder.isPresent(), "O pedido não foi encontrado no banco de dados");

        Order order = savedOrder.get();
        assertEquals(1, order.getItem().size(), "O pedido não contém a quantidade esperada de itens.");
        assertEquals(itemUuid, order.getItem().getFirst().getUuid(), "O item no pedido não corresponde ao esperado.");
    }


    @Test
    public void testAuthenticateUser() throws Exception {

        String authToken = secretKey;

        String responseBody = mockMvc.perform(post("http://localhost:8080/auth/token")
                        .header("Authorization", "Bearer " + authToken)
                        .header("accept", "*/*")
                        .content(""))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println("Corpo da resposta: " + responseBody);

        assertNotNull("O corpo da resposta não deve ser nulo", responseBody);
    }

    @AfterEach
    void cleanUp() {
        itemMongoRepository.deleteAll();
        orderMongoRepository.deleteAll();
    }

}

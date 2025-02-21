package org.test.order;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.test.order.infra.kafka.consumers.ItemConsumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderApplicationTest {
    // Application successfully initializes and starts the Spring context
    @Test
    public void test_application_starts_successfully() {
        ItemConsumer mockConsumer = mock(ItemConsumer.class);

        OrderApplication application = new OrderApplication(mockConsumer);

        application.startConsumer();

        verify(mockConsumer, timeout(1000).times(1)).runConsumer();
    }

    // Handle ItemConsumer initialization failure
    @Test
    public void test_consumer_initialization_failure() {
        ItemConsumer mockConsumer = mock(ItemConsumer.class);
        doThrow(new RuntimeException("Consumer initialization failed"))
                .when(mockConsumer)
                .runConsumer();

        OrderApplication application = new OrderApplication(mockConsumer);

        assertDoesNotThrow(application::startConsumer);

        verify(mockConsumer, timeout(1000).times(1)).runConsumer();
    }

    // Consumer thread starts correctly after application initialization
    @Test
    public void test_consumer_thread_starts_on_initialization() {
        ItemConsumer mockConsumer = Mockito.mock(ItemConsumer.class);
        OrderApplication app = new OrderApplication(mockConsumer);
        app.startConsumer();
        Mockito.verify(mockConsumer, timeout(1000).times(1)).runConsumer();
    }

    // ItemConsumer is properly injected via constructor dependency injection
    @Test
    public void test_item_consumer_injection() {
        ItemConsumer mockConsumer = Mockito.mock(ItemConsumer.class);
        OrderApplication app = new OrderApplication(mockConsumer);
        Assertions.assertNotNull(app);
        Assertions.assertEquals(mockConsumer, app.consumer);
    }

}
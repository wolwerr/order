package org.test.order.domain.exception.order;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OrderNotFoundExceptionTest {

    // Create exception with message and verify message is correctly set
    @Test
    public void test_exception_message_is_set_correctly() {
        String expectedMessage = "Order with ID 123 not found";

        OrderNotFoundException exception = new OrderNotFoundException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }

    // Create exception with null message
    @Test
    public void test_exception_with_null_message() {
        OrderNotFoundException exception = new OrderNotFoundException(null);

        assertNull(exception.getMessage());
    }

}

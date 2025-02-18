package org.test.order.domain.exception.item;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ItemEmptyExceptionTest {

    // Verify exception is thrown with a custom message
    @Test
    public void test_exception_thrown_with_custom_message() {
        String expectedMessage = "Item cannot be empty";

        ItemEmptyException exception = new ItemEmptyException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }

    // Create exception with null message
    @Test
    public void test_exception_with_null_message() {
        ItemEmptyException exception = new ItemEmptyException(null);

        assertNull(exception.getMessage());
    }

}

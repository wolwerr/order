package org.test.order.domain.exception.item;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemDataExceptionTest {

    // Create exception with message and verify message is stored correctly
    @Test
    public void test_exception_stores_message() {
        String expectedMessage = "Invalid item data";

        ItemDataException exception = new ItemDataException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }

    // Create exception with empty string message
    @Test
    public void test_exception_with_empty_message() {
        String emptyMessage = "";

        ItemDataException exception = new ItemDataException(emptyMessage);

        assertEquals(emptyMessage, exception.getMessage());
    }
}

package org.test.order.domain.exception.item;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemNotFoundExceptionTest {

    // Constructor creates exception with provided message
    @Test
    public void test_constructor_creates_exception_with_message() {
        String expectedMessage = "Item with id 123 not found";

        ItemNotFoundException exception = new ItemNotFoundException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }

    // Constructor handles empty string message
    @Test
    public void test_constructor_handles_empty_message() {
        String emptyMessage = "";

        ItemNotFoundException exception = new ItemNotFoundException(emptyMessage);

        assertEquals(emptyMessage, exception.getMessage());
    }

}

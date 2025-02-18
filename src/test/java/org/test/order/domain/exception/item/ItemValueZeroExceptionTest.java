package org.test.order.domain.exception.item;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ItemValueZeroExceptionTest {

    // Constructor creates exception with provided message
    @Test
    public void test_constructor_creates_exception_with_message() {
        String expectedMessage = "Item value cannot be zero";

        ItemValueZeroException exception = new ItemValueZeroException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }

    // Constructor handles null message parameter
    @Test
    public void test_constructor_handles_null_message() {
        ItemValueZeroException exception = new ItemValueZeroException(null);

        assertNull(exception.getMessage());
    }

}

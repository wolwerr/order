package org.test.order.domain.generic.output;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OutputStatusTest {

    // Constructor correctly initializes all fields with valid input values
    @Test
    public void test_constructor_initializes_fields_correctly() {
        int code = 200;
        String codeName = "OK";
        String message = "Success";

        OutputStatus status = new OutputStatus(code, codeName, message);

        assertEquals(code, status.getCode());
        assertEquals(codeName, status.getCodeName());
        assertEquals(message, status.getMessage());
    }


    @Test
    public void test_equals_with_null_object() {
        OutputStatus status = new OutputStatus(404, "NOT_FOUND", "Resource not found");

        assertFalse(status.equals(null));
    }

}
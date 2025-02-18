package org.test.order.domain.generic.output;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OutputErrorTest {

    // Verify OutputError object is created with valid message and OutputStatus
    @Test
    public void test_create_output_error_with_valid_params() {
        String message = "Error message";
        OutputStatus status = new OutputStatus(500, "ERROR", "Internal error");

        OutputError error = new OutputError(message, status);

        assertEquals(message, error.getMensagem());
        assertEquals(status, error.getOutputStatus());
        assertEquals(status, error.getBody());
    }

    // Create OutputError with null message
    @Test
    public void test_create_output_error_with_null_message() {
        OutputStatus status = new OutputStatus(404, "NOT_FOUND", "Resource not found");

        OutputError error = new OutputError(null, status);

        assertNull(error.getMensagem());
        assertEquals(status, error.getOutputStatus());
        assertEquals(status, error.getBody());
    }


}
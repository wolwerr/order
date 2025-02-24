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

        assertFalse(status == null);
    }

    @Test
    public void test_equals_with_same_object() {
        OutputStatus status = new OutputStatus(404, "NOT_FOUND", "Resource not found");

        assertTrue(status.equals(status));
    }

    @Test
    public void test_equals_with_different_class() {
        OutputStatus status = new OutputStatus(404, "NOT_FOUND", "Resource not found");

        assertFalse(status.equals("Some String"));
    }

    @Test
    public void test_equals_with_equal_objects() {
        OutputStatus status1 = new OutputStatus(404, "NOT_FOUND", "Resource not found");
        OutputStatus status2 = new OutputStatus(404, "NOT_FOUND", "Resource not found");

        assertTrue(status1.equals(status2));
    }

    @Test
    public void test_equals_with_unequal_objects() {
        OutputStatus status1 = new OutputStatus(404, "NOT_FOUND", "Resource not found");
        OutputStatus status2 = new OutputStatus(500, "INTERNAL_SERVER_ERROR", "Internal server error");

        assertFalse(status1.equals(status2));
    }

    @Test
    public void test_hashCode_with_equal_objects() {
        OutputStatus status1 = new OutputStatus(404, "NOT_FOUND", "Resource not found");
        OutputStatus status2 = new OutputStatus(404, "NOT_FOUND", "Resource not found");

        assertEquals(status1.hashCode(), status2.hashCode());
    }

    @Test
    public void test_hashCode_with_unequal_objects() {
        OutputStatus status1 = new OutputStatus(404, "NOT_FOUND", "Resource not found");
        OutputStatus status2 = new OutputStatus(500, "INTERNAL_SERVER_ERROR", "Internal server error");

        assertNotEquals(status1.hashCode(), status2.hashCode());
    }

    @Test
    public void test_toString() {
        OutputStatus status = new OutputStatus(404, "NOT_FOUND", "Resource not found");
        String expectedString = "OutputStatus{code=404, codeName='NOT_FOUND', message='Resource not found'}";

        assertEquals(expectedString, status.toString());
    }
}
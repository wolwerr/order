package org.test.order.domain.enuns;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class StatusOrderTest {

    // Verify enum contains PENDING status
    @Test
    public void test_pending_status_exists() {
        StatusOrder status = StatusOrder.PENDING;

        assertEquals(StatusOrder.PENDING, status);
        assertTrue(Arrays.asList(StatusOrder.values()).contains(StatusOrder.PENDING));
    }

    // Verify enum contains APPROVED status
    @Test
    public void test_approved_status_exists() {
        StatusOrder status = StatusOrder.APPROVED;

        assertEquals(StatusOrder.APPROVED, status);
        assertTrue(Arrays.asList(StatusOrder.values()).contains(StatusOrder.APPROVED));
    }

    // Verify enum contains REJECTED status
    @Test
    public void test_rejected_status_exists() {
        StatusOrder status = StatusOrder.REJECTED;

        assertEquals(StatusOrder.REJECTED, status);
        assertTrue(Arrays.asList(StatusOrder.values()).contains(StatusOrder.REJECTED));
    }

    // Attempt to create enum with invalid status string
    @Test
    public void test_invalid_status_throws_exception() {
        assertThrows(IllegalArgumentException.class, () -> {
            StatusOrder.valueOf("INVALID_STATUS");
        });
    }
}

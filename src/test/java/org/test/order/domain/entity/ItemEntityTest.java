package org.test.order.domain.entity;

import org.junit.jupiter.api.Test;
import org.test.order.domain.exception.item.ItemEmptyException;
import org.test.order.domain.exception.item.ItemValueZeroException;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ItemEntityTest {

    // Create ItemEntity with valid positive value and quantity
    @Test
    public void test_create_item_entity_with_valid_values() throws ItemValueZeroException, ItemEmptyException {
        UUID uuid = UUID.randomUUID();
        String name = "Test Item";
        Double value = 10.0;
        Integer quantity = 5;
        LocalDateTime now = LocalDateTime.now();

        ItemEntity item = new ItemEntity(uuid, name, value, quantity, now, now);

        assertEquals(10.0, item.verifyValue());
        assertEquals(5, item.verifyQuantity());
        assertEquals(uuid, item.getUuid());
        assertEquals(name, item.getName());
        assertEquals(now, item.getCreatedAt());
        assertEquals(now, item.getUpdatedAt());
    }

    // Return positive double value when value is greater than 0
    @Test
    public void test_verify_value_returns_positive_value() throws ItemValueZeroException, ItemEmptyException {
        ItemEntity item = new ItemEntity(UUID.randomUUID(), "Test Item", 10.0, 1, LocalDateTime.now(), LocalDateTime.now());

        double result = item.verifyValue();

        assertEquals(10.0, result);
    }

    // Throw ItemValueZero exception when value is negative
    @Test
    public void test_verify_value_throws_exception_for_negative_value() throws ItemValueZeroException, ItemEmptyException {
        ItemEntity item = new ItemEntity(UUID.randomUUID(), "Test Item", -10.0, 1, LocalDateTime.now(), LocalDateTime.now());

        assertThrows(ItemValueZeroException.class, item::verifyValue);
    }

    // Return quantity value when it is positive
    @Test
    public void test_verify_quantity_returns_positive_value() throws ItemEmptyException, ItemValueZeroException {
        ItemEntity item = new ItemEntity(UUID.randomUUID(), "Test Item", 10.0, 5, LocalDateTime.now(), LocalDateTime.now());

        int result = item.verifyQuantity();

        assertEquals(5, result);
    }

    // Throw ItemEmpty exception when quantity is negative
    @Test
    public void test_verify_quantity_throws_exception_for_negative() throws ItemValueZeroException, ItemEmptyException {
        ItemEntity item = new ItemEntity(UUID.randomUUID(), "Test Item", 10.0, -1, LocalDateTime.now(), LocalDateTime.now());

        ItemEmptyException exception = assertThrows(ItemEmptyException.class, item::verifyQuantity);

        assertEquals("Quantity must be greater than 0", exception.getMessage());
    }

}
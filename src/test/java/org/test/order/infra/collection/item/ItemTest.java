package org.test.order.infra.collection.item;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    // Create new Item with all valid parameters
    @Test
    public void test_create_item_with_valid_parameters() {
        UUID uuid = UUID.randomUUID();
        String name = "Test Item";
        Double value = 10.0;
        Integer quantity = 5;
        LocalDateTime now = LocalDateTime.now();

        Item item = new Item(uuid, name, value, quantity, now, now);

        assertNotNull(item);
        assertEquals(uuid, item.getUuid());
        assertEquals(name, item.getName());
        assertEquals(value, item.getValue());
        assertEquals(quantity, item.getQuantity());
        assertEquals(now, item.getCreatedAt());
        assertEquals(now, item.getUpdatedAt());
    }

}
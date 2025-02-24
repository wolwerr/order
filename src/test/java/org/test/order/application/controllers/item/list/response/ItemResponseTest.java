package org.test.order.application.controllers.item.list.response;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemResponseTest {

    @Test
    void testItemResponseConstructor() {
        UUID uuid = UUID.randomUUID();
        String name = "Item1";
        int quantity = 2;
        Double value = 10.0;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        ItemResponse itemResponse = new ItemResponse(uuid, name, quantity, value, createdAt, updatedAt);

        assertEquals(uuid, itemResponse.getUuid());
        assertEquals(name, itemResponse.getName());
        assertEquals(quantity, itemResponse.getQuantity());
        assertEquals(value, itemResponse.getValue());
        assertEquals(createdAt, itemResponse.getCreatedAt());
        assertEquals(updatedAt, itemResponse.getUpdatedAt());
    }
}
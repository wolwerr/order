package org.test.order.domain.output.Item;

import org.junit.jupiter.api.Test;
import org.test.order.domain.entity.ItemEntity;
import org.test.order.domain.exception.item.ItemEmptyException;
import org.test.order.domain.exception.item.ItemValueZeroException;
import org.test.order.domain.generic.output.OutputStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ListAllItemsOutputTest {

    // Constructor correctly initializes listItems and outputStatus
    @Test
    public void test_constructor_initializes_fields_correctly() throws ItemValueZeroException, ItemEmptyException {
        List<ItemEntity> items = Arrays.asList(
                new ItemEntity(UUID.randomUUID(), "Item1", 10.0, 1, LocalDateTime.now(), LocalDateTime.now())
        );
        OutputStatus status = new OutputStatus(200, "OK", "Success");

        ListAllItemsOutput output = new ListAllItemsOutput(items, status);

        assertEquals(items, output.getListItems());
        assertEquals(status, output.getOutputStatus());
        assertEquals(items, output.getBody());
    }

    // Constructor handles empty list of items
    @Test
    public void test_constructor_handles_empty_list() {
        List<ItemEntity> emptyList = new ArrayList<>();
        OutputStatus status = new OutputStatus(200, "OK", "Success");

        ListAllItemsOutput output = new ListAllItemsOutput(emptyList, status);

        assertTrue(output.getListItems().isEmpty());
        assertEquals(status, output.getOutputStatus());
        assertTrue(((List<ItemEntity>)output.getBody()).isEmpty());
    }


}
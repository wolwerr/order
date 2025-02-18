package org.test.order.domain.useCase.item;

import org.junit.jupiter.api.Test;
import org.test.order.domain.entity.ItemEntity;
import org.test.order.domain.exception.item.ItemEmptyException;
import org.test.order.domain.exception.item.ItemValueZeroException;
import org.test.order.domain.gateway.item.ListItemIterface;
import org.test.order.domain.generic.output.OutputError;
import org.test.order.domain.output.Item.ListAllItemsOutput;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListAllItemsUseCaseTest {

    // Successfully retrieves and returns list of items with 200 status code
    @Test
    public void test_execute_returns_list_items_with_success_status() throws ItemValueZeroException, ItemEmptyException {
        // Arrange
        ListItemIterface listItemIterface = mock(ListItemIterface.class);
        ListAllItemsUseCase listAllItemsUseCase = new ListAllItemsUseCase(listItemIterface);
        List<ItemEntity> expectedItems = Arrays.asList(
                new ItemEntity(UUID.randomUUID(), "Item 1", 10.0, 1, LocalDateTime.now(), LocalDateTime.now())
        );

        when(listItemIterface.findListaItens()).thenReturn(expectedItems);

        // Act
        listAllItemsUseCase.execute();

        // Assert
        ListAllItemsOutput output = (ListAllItemsOutput) listAllItemsUseCase.getListAllItemsOutput();
        assertEquals(200, output.getOutputStatus().getCode());
        assertEquals("OK", output.getOutputStatus().getCodeName());
        assertEquals(expectedItems, output.getListItems());
    }

    // Handles null response from listItemIterface.findListaItens()
    @Test
    public void test_execute_handles_null_response_with_error_status() throws ItemValueZeroException, ItemEmptyException {
        // Arrange
        ListItemIterface listItemIterface = mock(ListItemIterface.class);
        ListAllItemsUseCase listAllItemsUseCase = new ListAllItemsUseCase(listItemIterface);

        when(listItemIterface.findListaItens()).thenReturn(null);

        // Act
        listAllItemsUseCase.execute();

        // Assert
        OutputError output = (OutputError) listAllItemsUseCase.getListAllItemsOutput();
        assertEquals(500, output.getOutputStatus().getCode());
        assertEquals("INTERNAL_SERVER_ERROR", output.getOutputStatus().getCodeName());
        assertEquals("Error to list items", output.getMensagem());
    }

}
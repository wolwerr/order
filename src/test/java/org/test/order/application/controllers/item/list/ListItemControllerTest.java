package org.test.order.application.controllers.item.list;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.test.order.infra.collection.item.Item;
import org.test.order.infra.repository.ItemMongoRepository;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ListItemControllerTest {

    // Returns 200 status code and list of items when repository successfully returns data
    @Test
    public void test_get_all_items_returns_success_response() {
        // Arrange
        ItemMongoRepository mockRepo = mock(ItemMongoRepository.class);
        List<Item> mockItems = Arrays.asList(
                new Item(UUID.randomUUID(), "Item1", 10.00, 100, LocalDateTime.now(), LocalDateTime.now()),
                new Item(UUID.randomUUID(), "Item2", 5.00, 50, LocalDateTime.now(), LocalDateTime.now())
        );
        when(mockRepo.findAll()).thenReturn(mockItems);

        ListItemController controller = new ListItemController(mockRepo);

        // Act
        ResponseEntity<Object> response = controller.getAllItems();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(mockRepo).findAll();
    }

    // Handles case when repository returns null list of items

    @Test
    public void test_get_all_items_handles_null_list() {
        // Arrange
        ItemMongoRepository mockRepo = mock(ItemMongoRepository.class);
        when(mockRepo.findAll()).thenReturn(null);

        ListItemController controller = new ListItemController(mockRepo);

        // Act
        ResponseEntity<Object> response = controller.getAllItems();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(mockRepo).findAll();
    }

}
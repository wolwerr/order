package org.test.order.application.controllers.item.list;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.test.order.domain.exception.item.ItemEmptyException;
import org.test.order.domain.exception.item.ItemValueZeroException;
import org.test.order.domain.gateway.cache.CacheInterface;
import org.test.order.infra.collection.item.Item;
import org.test.order.infra.repository.ItemMongoRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListItemControllerTest {

    @Test
    void test_get_all_items_returns_success_response()  {
        // Arrange
        ItemMongoRepository mockRepo = mock(ItemMongoRepository.class);
        CacheInterface mockCache = mock(CacheInterface.class);
        ObjectMapper mockMapper = new ObjectMapper();
        mockMapper.registerModule(new JavaTimeModule());

        List<Item> mockItems = Arrays.asList(
                new Item(UUID.randomUUID(), "Item1", 10.00, 100, LocalDateTime.now(), LocalDateTime.now()),
                new Item(UUID.randomUUID(), "Item2", 5.00, 50, LocalDateTime.now(), LocalDateTime.now())
        );

        when(mockRepo.findAll()).thenReturn(mockItems);

        ListItemController controller = new ListItemController(mockRepo, mockCache, mockMapper);

        // Act
        ResponseEntity<Object> response = controller.getAllItems();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List<?>);
        List<?> responseBody = (List<?>) response.getBody();
        assertEquals(2, responseBody.size());

        verify(mockRepo).findAll();
    }

    @Test
    public void test_get_all_items_handles_null_list()  {
        // Arrange
        ItemMongoRepository mockRepo = mock(ItemMongoRepository.class);
        CacheInterface mockCache = mock(CacheInterface.class);
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        when(mockRepo.findAll()).thenReturn(null);

        ListItemController controller = new ListItemController(mockRepo, mockCache, mockMapper);

        // Act
        ResponseEntity<Object> response = controller.getAllItems();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(mockRepo).findAll();
    }
}
package org.test.order.infra.adpter.repository.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.test.order.domain.entity.ItemEntity;
import org.test.order.domain.exception.item.ItemEmptyException;
import org.test.order.domain.exception.item.ItemValueZeroException;
import org.test.order.infra.collection.item.Item;
import org.test.order.infra.repository.ItemMongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ListItemRepositoryTest {

    @Mock
    private ItemMongoRepository itemMongoRepository;

    private ListItemRepository listItemRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        listItemRepository = new ListItemRepository(itemMongoRepository);
    }

    // Repository successfully retrieves all items from MongoDB and converts them to ItemEntity list
    @Test
    public void test_find_lista_itens_success() throws ItemValueZeroException, ItemEmptyException {
        // Arrange
        UUID itemId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Item mockItem = new Item(itemId, "Test Item", 10.0, 5, now, now);
        List<Item> mockItems = List.of(mockItem);

        when(itemMongoRepository.findAll()).thenReturn(mockItems);

        // Act
        List<ItemEntity> result = listItemRepository.findListaItens();

        // Assert
        verify(itemMongoRepository).findAll();
        assertNotNull(result);
        assertEquals(1, result.size());

        ItemEntity resultItem = result.getFirst();
        assertEquals(itemId, resultItem.getUuid());
        assertEquals("Test Item", resultItem.getName());
        assertEquals(10.0, resultItem.getValue());
        assertEquals(5, resultItem.getQuantity());
        assertEquals(now, resultItem.getCreatedAt());
        assertEquals(now, resultItem.getUpdatedAt());
    }

    // Handle null values in Item fields during conversion to ItemEntity
    @Test
    public void test_find_lista_itens_with_null_fields() throws ItemValueZeroException, ItemEmptyException {
        // Arrange
        UUID itemId = UUID.randomUUID();
        Item mockItem = new Item(itemId, null, null, null, null, null);
        List<Item> mockItems = List.of(mockItem);

        when(itemMongoRepository.findAll()).thenReturn(mockItems);

        // Act
        List<ItemEntity> result = listItemRepository.findListaItens();

        // Assert
        verify(itemMongoRepository).findAll();
        assertNotNull(result);
        assertEquals(1, result.size());

        ItemEntity resultItem = result.getFirst();
        assertEquals(itemId, resultItem.getUuid());
        assertNull(resultItem.getName());
        assertEquals(0.0, resultItem.getValue());
        assertNull(resultItem.getQuantity());
        assertNull(resultItem.getCreatedAt());
        assertNull(resultItem.getUpdatedAt());
    }
}
package org.test.order.infra.kafka.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.test.order.domain.exception.item.ItemEmptyException;
import org.test.order.domain.exception.item.ItemValueZeroException;
import org.test.order.infra.collection.item.Item;
import org.test.order.infra.repository.ItemMongoRepository;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class ItemConsumerTest {


    @Test
    public void test_consume_and_save_valid_item() {
        // Arrange
        ItemMongoRepository mockRepo = mock(ItemMongoRepository.class);
        KafkaConsumer mockConsumer = mock(KafkaConsumer.class);

        ConsumerRecord<String, String> record = new ConsumerRecord<>(
                "item", 0, 0L, "key",
                "{\"uuid\":\"550e8400-e29b-41d4-a716-446655440000\"," +
                        "\"name\":\"test item\"," +
                        "\"value\":10.0," +
                        "\"quantity\":1," +
                        "\"createdAt\":\"2023-01-01T10:00:00\"," +
                        "\"updatedAt\":\"2023-01-01T10:00:00\"}"
        );

        ConsumerRecords<String, String> records = new ConsumerRecords<>(
                Collections.singletonMap(new TopicPartition("item", 0), Collections.singletonList(record))
        );

        when(mockConsumer.poll(any(Duration.class)))
                .thenReturn(records)
                .thenReturn(new ConsumerRecords<>(Collections.emptyMap()));

        when(mockRepo.findByUuid(any(UUID.class))).thenReturn(Optional.empty());

        ItemConsumer itemConsumer = new ItemConsumer(mockConsumer, mockRepo);

        // Act
        new Thread(itemConsumer::runConsumer).start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Assert
        verify(mockRepo).save(argThat(item ->
                item.getName().equals("test item") &&
                        item.getValue() == 10.0 &&
                        item.getQuantity() == 1
        ));

        mockConsumer.wakeup();
    }


    @Test
    public void test_handle_invalid_json_message() {
        // Arrange
        ItemMongoRepository mockRepo = mock(ItemMongoRepository.class);
        KafkaConsumer mockConsumer = mock(KafkaConsumer.class);

        ConsumerRecord<String, String> record = new ConsumerRecord<>(
                "item", 0, 0L, "key", "invalid json"
        );

        ConsumerRecords<String, String> records = new ConsumerRecords<>(
                Collections.singletonMap(new TopicPartition("item", 0), Collections.singletonList(record))
        );

        when(mockConsumer.poll(any(Duration.class)))
                .thenReturn(records)
                .thenReturn(new ConsumerRecords<>(Collections.emptyMap()));

        ItemConsumer itemConsumer = new ItemConsumer(mockConsumer, mockRepo);

        // Act
        new Thread(itemConsumer::runConsumer).start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Assert
        verify(mockRepo, never()).save(any(Item.class));

        mockConsumer.wakeup();
    }

    @Test
    public void test_should_not_save_existing_item() {
        // Arrange
        ItemMongoRepository mockRepo = mock(ItemMongoRepository.class);
        KafkaConsumer mockConsumer = mock(KafkaConsumer.class);

        UUID existingUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

        Item existingItem = new Item();
        existingItem.setUuid(existingUuid);

        ConsumerRecord<String, String> record = new ConsumerRecord<>(
                "item", 0, 0L, "key",
                "{\"uuid\":\"550e8400-e29b-41d4-a716-446655440000\"," +
                        "\"name\":\"test item\"," +
                        "\"value\":10.0," +
                        "\"quantity\":1," +
                        "\"createdAt\":\"2023-01-01T10:00:00\"," +
                        "\"updatedAt\":\"2023-01-01T10:00:00\"}"
        );

        ConsumerRecords<String, String> records = new ConsumerRecords<>(
                Collections.singletonMap(new TopicPartition("item", 0), Collections.singletonList(record))
        );

        when(mockConsumer.poll(any(Duration.class)))
                .thenReturn(records)
                .thenReturn(new ConsumerRecords<>(Collections.emptyMap()));

        when(mockRepo.findByUuid(existingUuid)).thenReturn(Optional.of(existingItem));

        ItemConsumer itemConsumer = new ItemConsumer(mockConsumer, mockRepo);

        // Act
        new Thread(itemConsumer::runConsumer).start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Assert
        verify(mockRepo, never()).save(any(Item.class));

        mockConsumer.wakeup();
    }

    @Test
    public void test_should_not_save_item_with_invalid_value() {
        // Arrange
        ItemMongoRepository mockRepo = mock(ItemMongoRepository.class);
        KafkaConsumer<String, String> mockConsumer = mock(KafkaConsumer.class);

        UUID uuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

        String invalidJson = "{\"uuid\":\"" + uuid + "\"," +
                "\"name\":\"test item\"," +
                "\"value\":-10.0," +  // Valor inválido
                "\"quantity\":1," +
                "\"createdAt\":\"2023-01-01T10:00:00\"," +
                "\"updatedAt\":\"2023-01-01T10:00:00\"}";

        ConsumerRecord<String, String> record = new ConsumerRecord<>(
                "item", 0, 0L, "key", invalidJson
        );

        ConsumerRecords<String, String> records = new ConsumerRecords<>(
                Collections.singletonMap(new TopicPartition("item", 0), Collections.singletonList(record))
        );

        when(mockConsumer.poll(any(Duration.class)))
                .thenReturn(records)
                .thenReturn(new ConsumerRecords<>(Collections.emptyMap()));

        ItemConsumer itemConsumer = new ItemConsumer(mockConsumer, mockRepo);

        // Act
        new Thread(itemConsumer::runConsumer).start();


        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Assert
        verify(mockRepo, never()).save(any(Item.class));

        mockConsumer.wakeup();
    }


    @Test
    public void test_should_not_save_item_with_invalid_quantity() {
        // Arrange
        ItemMongoRepository mockRepo = mock(ItemMongoRepository.class);
        KafkaConsumer<String, String> mockConsumer = mock(KafkaConsumer.class);

        UUID uuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

        String invalidJson = "{\"uuid\":\"" + uuid + "\"," +
                "\"name\":\"test item\"," +
                "\"value\":10.0," +  // Valor válido
                "\"quantity\":-1," +  // Quantidade inválida
                "\"createdAt\":\"2023-01-01T10:00:00\"," +
                "\"updatedAt\":\"2023-01-01T10:00:00\"}";

        ConsumerRecord<String, String> record = new ConsumerRecord<>(
                "item", 0, 0L, "key", invalidJson
        );

        ConsumerRecords<String, String> records = new ConsumerRecords<>(
                Collections.singletonMap(new TopicPartition("item", 0), Collections.singletonList(record))
        );

        when(mockConsumer.poll(any(Duration.class)))
                .thenReturn(records)
                .thenReturn(new ConsumerRecords<>(Collections.emptyMap()));

        ItemConsumer itemConsumer = new ItemConsumer(mockConsumer, mockRepo);

        // Act
        new Thread(itemConsumer::runConsumer).start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Assert
        verify(mockRepo, never()).save(any(Item.class));

        mockConsumer.wakeup();
    }

}

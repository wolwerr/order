package org.test.order.infra.kafka.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.test.order.domain.exception.item.ItemEmptyException;
import org.test.order.domain.exception.item.ItemValueZeroException;
import org.test.order.infra.collection.item.Item;
import org.test.order.infra.dependecy.kafka.resolvers.consumers.KafkaConsumerResolver;
import org.test.order.infra.repository.ItemMongoRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Component
public class ItemConsumer {
    private static final Logger logger = LoggerFactory.getLogger(ItemConsumer.class);
    private final KafkaConsumer<String, String> consumer;
    private final ObjectMapper objectMapper;
    private final ItemMongoRepository itemMongoRepository;

    public ItemConsumer(KafkaConsumer<String, String> consumer, ItemMongoRepository itemMongoRepository) {
        this.consumer = consumer;
        this.consumer.subscribe(Collections.singletonList(KafkaConsumerResolver.getItemConsumer()));
        this.itemMongoRepository = itemMongoRepository;
        this.objectMapper = new ObjectMapper();
    }

    public void runConsumer() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                if (!records.isEmpty()) {
                    processMessages(records);
                }
            }
        } finally {
            this.consumer.close();
            logger.info("Kafka closed.");
        }
    }

    @CircuitBreaker(name = "backendA", fallbackMethod = "fallback")
    @Retry(name = "backendA")
    public void processMessages(ConsumerRecords<String, String> records) {
        for (ConsumerRecord<String, String> record : records) {
            logger.info("Message received - Topic: {}, Key: {}, Value: {}", record.topic(), record.key(), record.value());
            try {
                if (record.value() != null && record.value().trim().endsWith("}")) {
                    JsonNode messageJson = objectMapper.readTree(record.value());
                    UUID uuidItem = UUID.fromString(messageJson.get("uuid").asText());

                    if (itemMongoRepository.findByUuid(uuidItem).isPresent()) {
                        logger.warn("Item with UUID {} already exists. Skipping save.", uuidItem);
                        continue;
                    }

                    String name = messageJson.get("name").asText();
                    double totalValue = messageJson.get("value").asDouble();
                    int quantity = messageJson.get("quantity").asInt();
                    LocalDateTime createdAt = LocalDateTime.parse(messageJson.get("createdAt").asText());
                    LocalDateTime updatedAt = LocalDateTime.parse(messageJson.get("updatedAt").asText());

                    if (totalValue < 0) {
                        throw new ItemValueZeroException("The value must be greater than 0");
                    }
                    if (quantity < 0) {
                        throw new ItemEmptyException("The quantity must be greater than 0");
                    }

                    Item item = new Item(uuidItem, name, totalValue, quantity, createdAt, updatedAt);
                    itemMongoRepository.save(item);
                    logger.info("Item with UUID {} saved successfully.", uuidItem);
                } else {
                    logger.error("Received incomplete JSON message: {}", record.value());
                }
            } catch (ItemValueZeroException | ItemEmptyException | JsonProcessingException e) {
                logger.error("Error processing message: {}", e.getMessage(), e);
            }
        }
    }

    public void fallback(Throwable t) {
        logger.error("Fallback method called due to: {}", t.getMessage(), t);
    }
}
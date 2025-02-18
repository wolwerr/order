package org.test.order.infra.kafka.consumers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Properties;
import java.util.UUID;

@Component
public class ItemConsumer {
    private static final Logger logger = LoggerFactory.getLogger(ItemConsumer.class);
    private final KafkaConsumer<String, String> consumer;
    private final ObjectMapper objectMapper;
    private final ItemMongoRepository itemMongoRepository;

    public ItemConsumer(
            Properties kafkaConsumerProperties,
            ItemMongoRepository itemMongoRepository
    ) {
        this.consumer = new KafkaConsumer<>(kafkaConsumerProperties);
        this.consumer.subscribe(Collections.singletonList(new KafkaConsumerResolver().getItemConsumer()));
        this.itemMongoRepository = itemMongoRepository;
        this.objectMapper = new ObjectMapper();
    }

    public void runConsumer() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    logger.info("Message receipted- Topic: {}, Key: {}, Value: {}", record.topic(), record.key(), record.value());
                    try {
                        JsonNode messageJson = objectMapper.readTree(record.value());
                        UUID uuidItem = UUID.fromString(messageJson.get("uuid").asText());
                        if (itemMongoRepository.findByUuid(uuidItem).isPresent()) {
                            logger.error("Item with UUID {} already exists. Skipping save.", uuidItem);
                            continue;
                        }
                        String name = messageJson.get("name").asText();
                        double totalValue = messageJson.get("value").asDouble();
                        int quantity = messageJson.get("quantity").asInt();
                        LocalDateTime created_at = LocalDateTime.parse(messageJson.get("createdAt").asText());
                        LocalDateTime updated_at = LocalDateTime.parse(messageJson.get("updatedAt").asText());

                        Item item = new Item();
                        item.setUuid(uuidItem);
                        item.setName(name);
                        item.setValue(totalValue);
                        item.setQuantity(quantity);
                        item.setCreatedAt(created_at);
                        item.setUpdatedAt(updated_at);

                        // Validate the item
                        if (totalValue < 0) {
                            throw new ItemValueZeroException("Value of item must be greater than 0");
                        }
                        if (quantity < 0) {
                            throw new ItemEmptyException("Quantity of item must be greater than 0");
                        }

                        itemMongoRepository.save(item);

                    } catch (Exception e) {
                        logger.error("Erro to process the message: {}", e.getMessage());
                    }
                }
            }
        } finally {
            this.consumer.close();
            logger.info("Kafka consumer closed.");
        }
    }
}
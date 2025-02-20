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
            boolean running = true;
            while (running && !Thread.currentThread().isInterrupted()) {

                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                if (records.isEmpty()) {
                    continue;
                }

                for (ConsumerRecord<String, String> record : records) {
                    logger.info("Mensagem recebida - Tópico: {}, Chave: {}, Valor: {}", record.topic(), record.key(), record.value());
                    try {
                        JsonNode messageJson = objectMapper.readTree(record.value());
                        UUID uuidItem = UUID.fromString(messageJson.get("uuid").asText());

                        if (itemMongoRepository.findByUuid(uuidItem).isPresent()) {
                            logger.error("Item com UUID {} já existe. Pulando salvamento.", uuidItem);
                            continue;
                        }

                        String name = messageJson.get("name").asText();
                        double totalValue = messageJson.get("value").asDouble();
                        int quantity = messageJson.get("quantity").asInt();
                        LocalDateTime createdAt = LocalDateTime.parse(messageJson.get("createdAt").asText());
                        LocalDateTime updatedAt = LocalDateTime.parse(messageJson.get("updatedAt").asText());

                        if (totalValue < 0) {
                            throw new ItemValueZeroException("O valor deve ser maior que 0");
                        }
                        if (quantity < 0) {
                            throw new ItemEmptyException("A quantidade deve ser maior que 0");
                        }

                        Item item = new Item(uuidItem, name, totalValue, quantity, createdAt, updatedAt);
                        itemMongoRepository.save(item);
                        logger.info("Item com UUID {} salvo com sucesso.", uuidItem);

                    } catch (Exception e) {

                        logger.error("Erro ao processar mensagem: {}", e.getMessage());
                    }
                }
            }
        } finally {

            this.consumer.close();
            logger.info("Kafka closed.");
        }
    }



}

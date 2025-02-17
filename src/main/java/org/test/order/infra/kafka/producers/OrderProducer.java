package org.test.order.infra.kafka.producers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.order.infra.collection.item.Item;
import org.test.order.infra.dependecy.kafka.resolvers.producers.KafkaProducerConfig;
import org.test.order.infra.dependecy.kafka.resolvers.producers.KafkaProducerResolver;

import java.util.UUID;

public class OrderProducer extends KafkaProducerConfig {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(OrderProducer.class);

    public OrderProducer(String servers) {
        super(servers, new KafkaProducerResolver().getOrder());
    }

    public void sendOrder(Item item) {
        try {
            ObjectNode jsonNode = objectMapper.createObjectNode();
            jsonNode.put("uuid", item.getUuid().toString());
            jsonNode.put("name", item.getName());
            jsonNode.put("value", item.getValue());
            jsonNode.put("quantity", item.getQuantity());
            jsonNode.put("createdAt", item.getCreatedAt().toString());
            jsonNode.put("updatedAt", item.getUpdatedAt().toString());
            String json = jsonNode.toString();
            send(UUID.randomUUID().toString(), json);
            logger.info("Message sent - UUID: {}, Name: {}, Value: {}, Quantity: {}",
                    item.getUuid(), item.getName(), item.getValue(), item.getQuantity());
        } catch (Exception e) {
            logger.error("Error sending message: ", e);
        }
    }
}
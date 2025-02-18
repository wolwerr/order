package org.test.order.infra.kafka.producers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.order.domain.entity.ItemEntity;
import org.test.order.domain.output.order.CreateOrderOutput;
import org.test.order.infra.dependecy.kafka.resolvers.producers.KafkaProducerConfig;
import org.test.order.infra.dependecy.kafka.resolvers.producers.KafkaProducerResolver;
import org.test.order.domain.entity.OrderEntity;

import java.util.UUID;

public class OrderProducer extends KafkaProducerConfig {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(OrderProducer.class);

    public OrderProducer(String servers) {
        super(servers, new KafkaProducerResolver().getOrder());
    }

    public void sendOrder(CreateOrderOutput createOrderOutput) {
        try {
            OrderEntity orderEntity = createOrderOutput.getOrderEntity();
            ObjectNode jsonNode = objectMapper.createObjectNode();
            jsonNode.put("uuid", orderEntity.getUuid().toString());
            jsonNode.put("orderNumber", orderEntity.getOrderNumber());
            jsonNode.put("statusOrder", orderEntity.getStatusOrder().toString());
            jsonNode.put("totalValue", orderEntity.getTotalValue());
            jsonNode.put("customerId", orderEntity.getCustomerId().toString());
            jsonNode.put("createdAt", orderEntity.getCreatedAt().toString());
            jsonNode.put("updatedAt", orderEntity.getUpdatedAt().toString());

            ArrayNode itemsNode = jsonNode.putArray("item");
            for (ItemEntity itemEntity : orderEntity.getItem()) {
                ObjectNode itemNode = objectMapper.createObjectNode();
                itemNode.put("uuid", itemEntity.getUuid().toString());
                itemNode.put("name", itemEntity.getName());
                itemNode.put("value", itemEntity.getValue());
                itemNode.put("quantity", itemEntity.getQuantity());
                itemNode.put("createdAt", itemEntity.getCreatedAt().toString());
                itemNode.put("updatedAt", itemEntity.getUpdatedAt().toString());
                itemsNode.add(itemNode);
            }

            ObjectNode responseNode = objectMapper.createObjectNode();
            responseNode.set("orderEntity", jsonNode);

            String json = responseNode.toString();
            send(UUID.randomUUID().toString(), json);
            logger.info("Message sent - UUID: {}, OrderNumber: {}, TotalValue: {}, Items: {}",
                    orderEntity.getUuid(), orderEntity.getOrderNumber(), orderEntity.getTotalValue(), orderEntity.getItem().size());
        } catch (Exception e) {
            logger.error("Error sending message: ", e);
        }
    }
    }

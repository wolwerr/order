package org.test.order.infra.kafka.producers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.test.order.domain.entity.ItemEntity;
import org.test.order.domain.output.order.CreateOrderOutput;
import org.test.order.domain.entity.OrderEntity;
import org.test.order.infra.collection.Fallback.FallbackEnum;
import org.test.order.infra.collection.Fallback.FallbackOrderEntity;
import org.test.order.infra.dependecy.kafka.resolvers.producers.KafkaProducerResolver;
import org.test.order.infra.repository.FallbackOrderRepository;

import java.util.UUID;

@Component
public class OrderProducer {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(OrderProducer.class);
    private final FallbackOrderRepository fallbackOrderRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic = KafkaProducerResolver.getOrder();

    public OrderProducer(KafkaTemplate<String, String> kafkaTemplate, FallbackOrderRepository fallbackOrderRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.fallbackOrderRepository = fallbackOrderRepository;
    }

    @CircuitBreaker(name = "kafkaProducer", fallbackMethod = "fallbackSendOrder")
    @Retry(name = "kafkaProducer")
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
            kafkaTemplate.send(topic, UUID.randomUUID().toString(), json);
            StringBuilder itemsInfo = new StringBuilder();
            for (ItemEntity item : orderEntity.getItem()) {
                itemsInfo.append(String.format("Item UUID: %s, Name: %s, Value: %s, Quantity: %s; ",
                        item.getUuid(), item.getName(), item.getValue(), item.getQuantity()));
            }

            logger.info("✅ Order sent - UUID: {}, OrderNumber: {}, TotalValue: {}, Items: {}",
                    orderEntity.getUuid(), orderEntity.getOrderNumber(), orderEntity.getTotalValue(), itemsInfo.toString());

        } catch (Exception e) {
            logger.error("❌ Error sending order to Kafka: ", e);
            throw e;
        }
    }

    public void fallbackSendOrder(CreateOrderOutput createOrderOutput, Throwable t) {
        logger.error("⚠️ Kafka unavailable! Storing order in fallback repository. Reason: ", t);

        OrderEntity order = createOrderOutput.getOrderEntity();
        FallbackOrderEntity fallbackOrder = convertToFallback(order);
        fallbackOrderRepository.save(fallbackOrder);
    }

    private FallbackOrderEntity convertToFallback(OrderEntity order) {
        FallbackOrderEntity fallbackOrder = new FallbackOrderEntity();
        fallbackOrder.setId(order.getUuid());
        fallbackOrder.setOrderNumber(order.getOrderNumber());
        fallbackOrder.setStatusOrder(order.getStatusOrder());
        fallbackOrder.setTotalValue(order.getTotalValue());
        fallbackOrder.setCustomerId(order.getCustomerId() != null ? order.getCustomerId().toString() : null);
        fallbackOrder.setCreatedAt(order.getCreatedAt());
        fallbackOrder.setUpdatedAt(order.getUpdatedAt());
        fallbackOrder.setItem(order.getItem());
        fallbackOrder.setFallbackStatus(FallbackEnum.FAILED);
        return fallbackOrder;
    }


}

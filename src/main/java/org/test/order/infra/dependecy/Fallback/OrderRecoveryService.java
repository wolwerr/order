package org.test.order.infra.dependecy.Fallback;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.test.order.domain.entity.OrderEntity;
import org.test.order.domain.generic.output.OutputStatus;
import org.test.order.domain.output.order.CreateOrderOutput;
import org.test.order.infra.collection.Fallback.FallbackEnum;
import org.test.order.infra.collection.Fallback.FallbackOrderEntity;
import org.test.order.infra.kafka.producers.OrderProducer;
import org.test.order.infra.repository.FallbackOrderRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderRecoveryService {
    private static final Logger logger = LoggerFactory.getLogger(OrderRecoveryService.class);
    private final FallbackOrderRepository fallbackOrderRepository;
    private final OrderProducer orderProducer;

    @Scheduled(fixedDelay = 60000)
    public void reprocessFailedOrders() {
        logger.info("🔄 Checking for failed orders to reprocess...");

        List<FallbackOrderEntity> failedOrders = fallbackOrderRepository.findByFallbackStatus(FallbackEnum.FAILED);
        for (FallbackOrderEntity fallbackOrder : failedOrders) {
            try {
                logger.info("📤 Retrying order: {}", fallbackOrder.getId());

                fallbackOrder.setFallbackStatus(FallbackEnum.PENDING);
                fallbackOrderRepository.save(fallbackOrder);

                OrderEntity orderEntity = new OrderEntity(
                        fallbackOrder.getId(),
                        fallbackOrder.getOrderNumber(),
                        fallbackOrder.getStatusOrder(),
                        fallbackOrder.getTotalValue(),
                        fallbackOrder.getCustomerId() != null ? UUID.fromString(fallbackOrder.getCustomerId()) : null,
                        fallbackOrder.getCreatedAt(),
                        fallbackOrder.getUpdatedAt(),
                        fallbackOrder.getItem(),
                        fallbackOrder.getFallbackStatus()
                );

                orderProducer.sendOrder(new CreateOrderOutput(orderEntity, new OutputStatus(201, "Created", "Order reprocessed successfully")));

                fallbackOrderRepository.delete(fallbackOrder);
                logger.info("✅ Order successfully reprocessed: {}", fallbackOrder.getId());

            } catch (Exception e) {
                logger.error("❌ Failed to reprocess order: {}", fallbackOrder.getId(), e);

                fallbackOrder.setFallbackStatus(FallbackEnum.FAILED);
                fallbackOrderRepository.save(fallbackOrder);
            }
        }
    }
}

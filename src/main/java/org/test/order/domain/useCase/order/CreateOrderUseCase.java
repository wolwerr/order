package org.test.order.domain.useCase.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.test.order.domain.entity.ItemEntity;
import org.test.order.domain.entity.OrderEntity;
import org.test.order.domain.exception.item.ItemEmptyException;
import org.test.order.domain.exception.item.ItemNotFoundException;
import org.test.order.domain.exception.item.ItemValueZeroException;
import org.test.order.domain.exception.order.OrderValueZeroException;
import org.test.order.domain.gateway.order.CreateOrderInterface;
import org.test.order.domain.generic.output.OutputError;
import org.test.order.domain.generic.output.OutputInterface;
import org.test.order.domain.generic.output.OutputStatus;
import org.test.order.domain.input.order.CreateOrderInput;
import org.test.order.domain.output.order.CreateOrderOutput;
import org.test.order.infra.kafka.producers.OrderProducer;
import org.test.order.infra.repository.ItemMongoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
@Component
public class CreateOrderUseCase {
    private static final Logger logger = LoggerFactory.getLogger(CreateOrderUseCase.class);
    private final CreateOrderInterface createOrderRepository;
    private final OrderProducer orderProducer;
    private final ItemMongoRepository itemMongoRepository;
    private OutputInterface createOrderOutput;

    @Transactional
    public void execute(CreateOrderInput createOrderInput) throws Exception {
        try {
            List<ItemEntity> itemEntities = createOrderInput.items().stream()
                    .map(item -> {
                        try {
                            var dbItem = itemMongoRepository.findById(item.uuid().toString())
                                    .orElseThrow(() -> new ItemNotFoundException("Item not found: " + item.uuid()));
                            ItemEntity itemEntity = new ItemEntity(
                                    dbItem.getUuid(),
                                    dbItem.getName(),
                                    dbItem.getValue(),
                                    item.quantity(),
                                    dbItem.getCreatedAt(),
                                    dbItem.getUpdatedAt()
                            );
                            itemEntity.verifyQuantity();
                            return itemEntity;
                        } catch (ItemEmptyException | ItemValueZeroException | ItemNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());

            double totalValue = itemEntities.stream()
                    .mapToDouble(item -> item.getValue() * item.getQuantity())
                    .sum();

            OrderEntity orderEntity = new OrderEntity(
                    createOrderInput.uuid(),
                    createOrderInput.orderNumber(),
                    createOrderInput.statusOrder(),
                    totalValue,
                    createOrderInput.customerId(),
                    createOrderInput.createdAt(),
                    createOrderInput.updatedAt(),
                    itemEntities
            );

            orderEntity.verifyTotalValue();

            if (!orderEntity.hasSufficientStock(itemMongoRepository)) {
                this.createOrderOutput = new OutputError(
                        "One or more items are out of stock.",
                        new OutputStatus(400, "Bad Request", "One or more items are out of stock.")
                );
                return;
            }

            this.createOrderRepository.saveOrder(orderEntity);
            this.createOrderOutput = new CreateOrderOutput(
                    orderEntity,
                    new OutputStatus(201, "Created", "Order created successfully")
            );

        } catch (OrderValueZeroException e) {
            logger.error("Order total value cannot be zero: {}", e.getMessage(), e);
            this.createOrderOutput = new OutputError(
                    e.getMessage(),
                    new OutputStatus(400, "Bad Request", "Order total value cannot be zero")
            );
        } catch (Exception e) {
            logger.error("Error creating order: {}", e.getMessage(), e);
            this.createOrderOutput = new OutputError(
                    e.getMessage(),
                    new OutputStatus(500, "Internal Server Error", "Error creating order")
            );
        } finally {
            if (this.createOrderOutput instanceof CreateOrderOutput createOrderOutput) {
                this.orderProducer.sendOrder(createOrderOutput);
            }
        }
    }
}
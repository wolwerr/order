package org.test.order.domain.useCase.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.test.order.domain.entity.ItemEntity;
import org.test.order.domain.entity.OrderEntity;
import org.test.order.domain.exception.item.ItemEmptyException;
import org.test.order.domain.exception.item.ItemValueZeroException;
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
public class CreateOrderUseCase {
    private static final Logger logger = LoggerFactory.getLogger(CreateOrderUseCase.class);
    private final CreateOrderInterface createOrderRepository;
    private final OrderProducer orderProducer;
    private final ItemMongoRepository itemMongoRepository;
    private OutputInterface createOrderOutput;

    @Transactional
    public void execute(CreateOrderInput createOrderInput) {
        try {
            List<ItemEntity> itemEntities = createOrderInput.items().stream()
                    .map(item -> {
                        try {
                            ItemEntity itemEntity = new ItemEntity(item.getUuid(), item.getName(), item.getValue(), item.getQuantity(), item.getCreatedAt(), item.getUpdatedAt());
                            itemEntity.verifyQuantity(); // Verify the quantity of the item
                            return itemEntity;
                        } catch (ItemValueZeroException | ItemEmptyException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());

            OrderEntity orderEntity = new OrderEntity(
                    createOrderInput.uuid(),
                    createOrderInput.orderNumber(),
                    createOrderInput.statusOrder(),
                    createOrderInput.totalValue(),
                    createOrderInput.customerId(),
                    createOrderInput.createdAt(),
                    createOrderInput.updatedAt(),
                    itemEntities
            );

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
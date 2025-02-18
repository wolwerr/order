package org.test.order.infra.adpter.repository.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.test.order.domain.entity.ItemEntity;
import org.test.order.domain.entity.OrderEntity;
import org.test.order.domain.exception.item.ItemEmptyException;
import org.test.order.domain.exception.item.ItemValueZeroException;
import org.test.order.domain.exception.order.OrderNotFoundException;
import org.test.order.domain.exception.order.OrderValueZeroException;
import org.test.order.domain.gateway.order.CreateOrderInterface;
import org.test.order.infra.collection.item.Item;
import org.test.order.infra.collection.order.Order;
import org.test.order.infra.repository.ItemMongoRepository;
import org.test.order.infra.repository.OrderMongoRepository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CreateOrderRepository implements CreateOrderInterface {

    private final OrderMongoRepository orderMongoRepository;
    private final ItemMongoRepository itemMongoRepository;

    public void saveOrder(OrderEntity orderEntity) throws OrderValueZeroException {
        if (orderEntity.getTotalValue() == 0.0) {
            throw new OrderValueZeroException("Order total value cannot be zero.");
        }

        if (orderEntity.getItem() == null) {
            orderEntity.setItem(new ArrayList<>());
        }

        Order orderCollection = new Order(
                orderEntity.getUuid(),
                orderEntity.getOrderNumber(),
                orderEntity.getStatusOrder(),
                orderEntity.getTotalValue(),
                orderEntity.getCustomerId(),
                orderEntity.getCreatedAt(),
                orderEntity.getUpdatedAt(),
                orderEntity.getItem().stream().map(itemEntity -> {
                    Optional<Item> itemOptional = itemMongoRepository.findByUuid(itemEntity.getUuid());
                    if (itemOptional.isEmpty()) {
                        throw new RuntimeException(new OrderNotFoundException("Item not found: " + itemEntity.getUuid()));
                    }
                    Item item = getItem(itemEntity, itemOptional);
                    itemMongoRepository.save(item);

                    return new Item(
                            itemEntity.getUuid(),
                            itemEntity.getName(),
                            itemEntity.getValue(),
                            itemEntity.getQuantity(),
                            itemEntity.getCreatedAt(),
                            itemEntity.getUpdatedAt()
                    );
                }).collect(Collectors.toList())
        );

        orderCollection = orderMongoRepository.save(orderCollection);

        orderEntity.setUuid(orderCollection.getUuid());
        orderEntity.setOrderNumber(orderCollection.getOrderNumber());
        orderEntity.setTotalValue(orderCollection.getTotalValue());
        orderEntity.setCustomerId(orderCollection.getCustomerId());
        orderEntity.setCreatedAt(orderCollection.getCreatedAt());
        orderEntity.setUpdatedAt(orderCollection.getUpdatedAt());
        orderEntity.setItem(orderCollection.getItem().stream().map(item -> {
            try {
                return new ItemEntity(
                        item.getUuid(),
                        item.getName(),
                        item.getValue(),
                        item.getQuantity(),
                        item.getCreatedAt(),
                        item.getUpdatedAt()
                );
            } catch (ItemValueZeroException | ItemEmptyException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));
    }

    private static Item getItem(ItemEntity itemEntity, Optional<Item> itemOptional) {
        Item item = itemOptional.get();
        int newQuantity = 0;
        try {
            newQuantity = item.verifyQuantity() - itemEntity.getQuantity();
        } catch (ItemEmptyException e) {
            throw new RuntimeException(e);
        }
        if (newQuantity < 0) {
            throw new RuntimeException(new OrderNotFoundException("Item out of stock: " + itemEntity.getUuid()));
        }
        item.setQuantity(newQuantity);
        return item;
    }
}
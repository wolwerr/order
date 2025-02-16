package org.test.order.infra.adpter.repository.order;

import lombok.RequiredArgsConstructor;
import org.test.order.domain.entity.ItemEntity;
import org.test.order.domain.entity.OrderEntity;
import org.test.order.domain.gateway.order.ListOrdersInterface;
import org.test.order.infra.collection.item.Item;
import org.test.order.infra.collection.order.Order;
import org.test.order.infra.repository.OrderMongoRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ListOrdersRepository  implements ListOrdersInterface {
    public  final OrderMongoRepository orderMongoRepository;

    private static List<ItemEntity> getItems(List<Item> itemsOrder) {
        List<ItemEntity> itemsList = new ArrayList<>();

        for (Item item : itemsOrder) {
            ItemEntity itemEntity = new ItemEntity(
                    item.getUuid(),
                    item.getName(),
                    item.getValue(),
                    item.getQuantity(),
                    item.getCreatedAt(),
                    item.getUpdatedAt()
            );
            itemsList.add(itemEntity);
        }
        return itemsList;
    }

    public List<OrderEntity> listOrders() {
        List<Order> orderModels = orderMongoRepository.findAll();
        List<OrderEntity> orderEntities = new ArrayList<>();

        for (Order orderCollection : orderModels) {
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setUuid(orderCollection.getUuid());
            orderEntity.setOrderNumber(orderCollection.getOrderNumber());
            orderEntity.setStatusOrder(orderCollection.getStatusOrder());
            orderEntity.setTotalValue(orderCollection.getTotalValue());
            orderEntity.setCustomerId(orderCollection.getCustomerId());
            orderEntity.setCreatedAt(orderCollection.getCreatedAt());
            orderEntity.setUpdatedAt(orderCollection.getUpdatedAt());

            List<ItemEntity> itemsList = getItems(orderCollection.getItems());
            orderEntity.setItem(itemsList);

            orderEntities.add(orderEntity);
        }
        return orderEntities;
    }
}

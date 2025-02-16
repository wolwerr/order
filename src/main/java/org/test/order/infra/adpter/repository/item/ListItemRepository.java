package org.test.order.infra.adpter.repository.item;

import lombok.RequiredArgsConstructor;
import org.test.order.domain.entity.ItemEntity;
import org.test.order.domain.gateway.item.ListItemIterface;
import org.test.order.infra.collection.item.Item;
import org.test.order.infra.repository.ItemMongoRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ListItemRepository implements ListItemIterface {
    private final ItemMongoRepository itemMongoRepository;

    public List<ItemEntity> findListaItens() {
        List<Item> itemModels = itemMongoRepository.findAll();
        List<ItemEntity> itemEntities = new ArrayList<>();

        for (Item itemCollection : itemModels) {
            ItemEntity itemEntity = new ItemEntity(
                    itemCollection.getUuid(),
                    itemCollection.getName(),
                    itemCollection.getValue(),
                    itemCollection.getQuantity(),
                    itemCollection.getCreatedAt(),
                    itemCollection.getUpdatedAt()
            );
            itemEntities.add(itemEntity);
        }
        return itemEntities;
    }
}
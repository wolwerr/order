package org.test.order.infra.adpter.repository.item;

import lombok.RequiredArgsConstructor;
import org.test.order.domain.entity.ItemEntity;
import org.test.order.domain.gateway.item.ListItemIterface;
import org.test.order.infra.collection.item.Item;
import org.test.order.infra.repository.ItemMongoRepository;
import java.util.List;

import static org.test.order.infra.adpter.repository.order.ListOrdersRepository.getItemEntities;

@RequiredArgsConstructor
public class ListItemRepository implements ListItemIterface {
    private final ItemMongoRepository itemMongoRepository;

    public List<ItemEntity> findListaItens() {
        List<Item> itemModels = itemMongoRepository.findAll();
        return getItemEntities(itemModels);
    }
}
package org.test.order.domain.gateway.item;

import org.test.order.domain.entity.ItemEntity;

import java.util.List;

public interface ListItemIterface {
    List<ItemEntity> findListaItens();
}

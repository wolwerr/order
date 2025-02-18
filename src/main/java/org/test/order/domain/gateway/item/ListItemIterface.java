package org.test.order.domain.gateway.item;

import org.test.order.domain.entity.ItemEntity;
import org.test.order.domain.exception.item.ItemEmptyException;
import org.test.order.domain.exception.item.ItemValueZeroException;

import java.util.List;

public interface ListItemIterface {
    List<ItemEntity> findListaItens() throws ItemValueZeroException, ItemEmptyException;
}

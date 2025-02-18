package org.test.order.domain.gateway.order;

import org.test.order.domain.entity.OrderEntity;
import org.test.order.domain.exception.item.ItemEmptyException;
import org.test.order.domain.exception.item.ItemValueZeroException;

import java.util.List;

public interface ListOrdersInterface {
    List<OrderEntity> listOrders() throws ItemValueZeroException, ItemEmptyException;
}

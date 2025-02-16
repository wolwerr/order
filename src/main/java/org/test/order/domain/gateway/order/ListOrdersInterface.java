package org.test.order.domain.gateway.order;

import org.test.order.domain.entity.OrderEntity;

import java.util.List;

public interface ListOrdersInterface {
    List<OrderEntity> listOrders();
}

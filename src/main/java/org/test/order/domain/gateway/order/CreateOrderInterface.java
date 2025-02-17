package org.test.order.domain.gateway.order;

import org.test.order.domain.entity.OrderEntity;
import org.test.order.domain.exception.order.OrderNotFoundException;


public interface CreateOrderInterface {
    void saveOrder(OrderEntity orderEntity) throws OrderNotFoundException;

}
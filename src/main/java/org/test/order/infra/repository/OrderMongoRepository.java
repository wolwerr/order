package org.test.order.infra.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.test.order.infra.collection.order.Order;

import java.util.UUID;

public interface OrderMongoRepository  extends MongoRepository<Order, String> {
    Order findByUuid(UUID uuid);
    Order findByOrderNumber(String orderNumber);
    void deleteByUuid(UUID uuid);

}

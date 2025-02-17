package org.test.order.infra.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.test.order.infra.collection.item.Item;

import java.util.Optional;
import java.util.UUID;

public interface ItemMongoRepository extends MongoRepository<Item, String> {
    Optional<Item> findByUuid(UUID uuid);
}
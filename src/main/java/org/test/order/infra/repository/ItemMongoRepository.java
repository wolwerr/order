package org.test.order.infra.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.test.order.infra.collection.item.Item;

public interface ItemMongoRepository  extends MongoRepository<Item, String> {

}

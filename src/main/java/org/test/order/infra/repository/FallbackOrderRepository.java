package org.test.order.infra.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.test.order.infra.collection.Fallback.FallbackEnum;
import org.test.order.infra.collection.Fallback.FallbackOrderEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface FallbackOrderRepository extends MongoRepository<FallbackOrderEntity, UUID> {
    List<FallbackOrderEntity> findByFallbackStatus(FallbackEnum fallbackStatus);
}


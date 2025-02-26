package org.test.order.infra.collection.Fallback;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.test.order.domain.entity.ItemEntity;
import org.test.order.domain.enuns.StatusOrder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "fallback_orders")
public class FallbackOrderEntity {
    @Id
    private UUID id;
    private String orderNumber;
    private StatusOrder statusOrder;
    private Double totalValue;
    private String customerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ItemEntity> item;
    private FallbackEnum fallbackStatus;

}




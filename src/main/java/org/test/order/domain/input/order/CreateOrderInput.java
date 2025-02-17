package org.test.order.domain.input.order;


import org.test.order.domain.enuns.StatusOrder;
import org.test.order.infra.collection.item.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateOrderInput (
     UUID uuid,
     String orderNumber,
     StatusOrder statusOrder,
     Double totalValue,
     UUID customerId,
     LocalDateTime createdAt,
     LocalDateTime updatedAt,
     List<Item> items
) {
    }
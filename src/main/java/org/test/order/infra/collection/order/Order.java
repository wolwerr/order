package org.test.order.infra.collection.order;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.test.order.domain.enuns.StatusOrder;
import org.test.order.infra.collection.item.Item;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Document(collection = "order")
@Getter
@Setter
public class Order implements Serializable {
    @Id
    @Field("uuid")
    private UUID uuid;
    @Field("order_number")
    private String orderNumber;
    @Field("StatusOrder")
    private StatusOrder statusOrder;
    @Field("total_value")
    private Double totalValue;
    @Field("customer_id")
    private UUID customerId;
    @Field("items")
    private List<Item> items;
    @CreatedDate
    @Field("createdAt")
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Field("updatedAt")
    private LocalDateTime updatedAt;
}
package org.test.order.infra.collection.item;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "item")
@Getter
@Setter
public class Item {
    @Id
    @Field("uuid")
    private UUID uuid;
    @Field("name")
    private String name;
    @Field("value")
    private Double value;
    @Field("quantity")
    private Integer quantity;
    @CreatedDate
    @Field("createdAt")
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Field("updatedAt")
    private LocalDateTime updatedAt;
}
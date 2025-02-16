package org.test.order.infra.collection.item;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Getter
@Setter
public class Item {
    private UUID uuid;
    @Field("name")
    private String name;
    @Field("value")
    private Double value;
    @Field("quantity")
    private Integer quantity;


    public Item(UUID uuid, String name, Double value, Integer quantity) {
        this.uuid = uuid;
        this.name = name;
        this.value = value;
        this.quantity = quantity;
    }
}

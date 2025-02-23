package org.test.order.application.controllers.item.list.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {
    private UUID uuid;
    private String name;
    private int quantity;
    private Double value;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
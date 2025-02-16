package org.test.order.domain.exception.item;

public class ItemNotFound  extends RuntimeException {
    public ItemNotFound(String message) {
        super(message);
    }
}

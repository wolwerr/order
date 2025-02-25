package org.test.order.domain.exception.item;

public class ItemEmptyException extends RuntimeException {
    public ItemEmptyException(String message) {
        super(message);
    }
}

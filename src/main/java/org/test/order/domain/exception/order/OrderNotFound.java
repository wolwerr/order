package org.test.order.domain.exception.order;

public class OrderNotFound  extends RuntimeException {
    public OrderNotFound(String message) {
        super(message);
    }
}

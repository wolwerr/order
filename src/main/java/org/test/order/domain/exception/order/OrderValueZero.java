package org.test.order.domain.exception.order;

public class OrderValueZero extends RuntimeException {
    public OrderValueZero(String message) {
        super(message);
    }
}

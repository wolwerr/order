package org.test.order.domain.output.order;

import org.junit.jupiter.api.Test;
import org.test.order.domain.entity.OrderEntity;
import org.test.order.domain.generic.output.OutputStatus;

import static org.junit.jupiter.api.Assertions.*;

class CreateOrderOutputTest {

    // Constructor initializes OrderEntity and OutputStatus correctly
    @Test
    public void test_constructor_initializes_fields_correctly() {
        OrderEntity orderEntity = new OrderEntity();
        OutputStatus outputStatus = new OutputStatus(200, "SUCCESS", "Order created");

        CreateOrderOutput output = new CreateOrderOutput(orderEntity, outputStatus);

        assertEquals(orderEntity, output.getOrderEntity());
        assertEquals(outputStatus, output.getOutputStatus());
        assertNull(output.getBody());
    }

    // Constructor handles null OrderEntity
    @Test
    public void test_constructor_accepts_null_order_entity() {
        OutputStatus outputStatus = new OutputStatus(404, "NOT_FOUND", "Order not found");

        CreateOrderOutput output = new CreateOrderOutput(null, outputStatus);

        assertNull(output.getOrderEntity());
        assertEquals(outputStatus, output.getOutputStatus());
        assertNull(output.getBody());
    }


}
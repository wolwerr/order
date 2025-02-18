package org.test.order.domain.output.order;

import org.junit.jupiter.api.Test;
import org.test.order.domain.entity.OrderEntity;
import org.test.order.domain.enuns.StatusOrder;
import org.test.order.domain.generic.output.OutputStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ListAllOrdersOutputTest {

    // Constructor correctly initializes ListAllOrdersOutput with list of orders and output status
    @Test
    public void test_constructor_initializes_with_valid_orders_and_status() {
        List<OrderEntity> orders = Arrays.asList(
                new OrderEntity(UUID.randomUUID(), "123", StatusOrder.PENDING, 100.0,
                        UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now(), new ArrayList<>())
        );

        OutputStatus status = new OutputStatus(200, "SUCCESS", "Orders retrieved");

        ListAllOrdersOutput output = new ListAllOrdersOutput(orders, status);

        assertEquals(orders, output.getListOrders());
        assertEquals(status, output.getOutputStatus());
        assertEquals(orders, output.getBody());
    }

    // Constructor handles empty list of orders
    @Test
    public void test_constructor_handles_empty_orders_list() {
        List<OrderEntity> emptyOrders = new ArrayList<>();
        OutputStatus status = new OutputStatus(200, "SUCCESS", "No orders found");

        ListAllOrdersOutput output = new ListAllOrdersOutput(emptyOrders, status);

        assertTrue(output.getListOrders().isEmpty());
        assertEquals(status, output.getOutputStatus());
        assertTrue(((List<OrderEntity>)output.getBody()).isEmpty());
    }

}
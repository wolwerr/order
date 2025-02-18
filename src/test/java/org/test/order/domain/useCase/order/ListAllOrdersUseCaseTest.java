package org.test.order.domain.useCase.order;

import org.junit.jupiter.api.Test;
import org.test.order.domain.entity.OrderEntity;
import org.test.order.domain.exception.item.ItemEmptyException;
import org.test.order.domain.exception.item.ItemValueZeroException;
import org.test.order.domain.gateway.order.ListOrdersInterface;
import org.test.order.domain.generic.output.OutputError;
import org.test.order.domain.generic.output.OutputInterface;
import org.test.order.domain.output.order.ListAllOrdersOutput;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListAllOrdersUseCaseTest {

    // Successfully retrieve and return list of orders with 200 status code
    @Test
    public void test_execute_should_return_list_orders_successfully() throws ItemValueZeroException, ItemEmptyException {
        // Arrange
        List<OrderEntity> mockOrders = Arrays.asList(new OrderEntity(), new OrderEntity());
        ListOrdersInterface mockListOrdersInterface = mock(ListOrdersInterface.class);
        when(mockListOrdersInterface.listOrders()).thenReturn(mockOrders);

        ListAllOrdersUseCase useCase = new ListAllOrdersUseCase(mockListOrdersInterface);

        // Act
        useCase.execute();

        // Assert
        OutputInterface output = useCase.getListAllOrdersOutput();
        assertNotNull(output);
        assertTrue(output instanceof ListAllOrdersOutput);
        assertEquals(200, ((ListAllOrdersOutput) output).getOutputStatus().getCode());
        assertEquals(mockOrders, ((ListAllOrdersOutput) output).getListOrders());
    }

    // ListOrdersInterface returns null list
    @Test
    public void test_execute_should_handle_null_list_orders() throws ItemValueZeroException, ItemEmptyException {
        // Arrange
        ListOrdersInterface mockListOrdersInterface = mock(ListOrdersInterface.class);
        when(mockListOrdersInterface.listOrders()).thenReturn(null);

        ListAllOrdersUseCase useCase = new ListAllOrdersUseCase(mockListOrdersInterface);

        // Act
        useCase.execute();

        // Assert
        OutputInterface output = useCase.getListAllOrdersOutput();
        assertNotNull(output);
        assertTrue(output instanceof OutputError);
        assertEquals(500, ((OutputError) output).getOutputStatus().getCode());
        assertEquals("Error to list orders", ((OutputError) output).getMensagem());
    }

}
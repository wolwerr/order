package org.test.order.domain.useCase.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.test.order.domain.entity.OrderEntity;
import org.test.order.domain.gateway.cache.CacheInterface;
import org.test.order.domain.gateway.order.ListOrdersInterface;
import org.test.order.domain.generic.output.OutputError;
import org.test.order.domain.generic.output.OutputInterface;
import org.test.order.domain.generic.output.OutputStatus;
import org.test.order.domain.output.order.ListAllOrdersOutput;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ListAllOrdersUseCase {
    private final ListOrdersInterface listOrdersInterface;
    private final CacheInterface cacheInterface;
    private OutputInterface listAllOrdersOutput;

    public void execute() {
        String cacheKey = "listAllOrders";
        try {
            List<OrderEntity> listOrders = (List<OrderEntity>) cacheInterface.getDataFromCache(cacheKey);

            if (listOrders == null) {
                listOrders = listOrdersInterface.listOrders();
                if (listOrders == null) {
                    throw new Exception("Error to list orders");
                }
                cacheInterface.cacheData(cacheKey, listOrders);
            }

            listAllOrdersOutput = new ListAllOrdersOutput(
                    listOrders,
                    new OutputStatus(200, "OK", "List orders successfully")
            );

        } catch (Exception e) {
            listAllOrdersOutput = new OutputError(
                    e.getMessage(),
                    new OutputStatus(500, "INTERNAL_SERVER_ERROR", "Error to list orders")
            );
        }
    }
}
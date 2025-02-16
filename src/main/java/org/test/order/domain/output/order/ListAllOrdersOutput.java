package org.test.order.domain.output.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.test.order.domain.entity.OrderEntity;
import org.test.order.domain.generic.output.OutputInterface;
import org.test.order.domain.generic.output.OutputStatus;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class ListAllOrdersOutput implements OutputInterface {
    private List<OrderEntity> listOrders;
    private OutputStatus outputStatus;

    public ListAllOrdersOutput(List<OrderEntity> listOrders, OutputStatus outputStatus) {
        this.listOrders = listOrders;
        this.outputStatus = outputStatus;
    }

    @Override
    public Object getBody() {
        return this.listOrders;
    }

}

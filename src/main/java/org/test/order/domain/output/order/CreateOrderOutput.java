package org.test.order.domain.output.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.test.order.domain.entity.OrderEntity;
import org.test.order.domain.generic.output.OutputInterface;
import org.test.order.domain.generic.output.OutputStatus;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class CreateOrderOutput implements OutputInterface {
    private final OrderEntity orderEntity;
    private Object body;
    private final OutputStatus outputStatus;

    @Override
    public OutputStatus getOutputStatus() {
        return outputStatus;
    }

    @Override
    public Object getBody() {
        return body;
    }
}
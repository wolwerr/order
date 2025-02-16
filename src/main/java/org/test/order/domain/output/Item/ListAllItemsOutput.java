package org.test.order.domain.output.Item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.test.order.domain.entity.ItemEntity;
import org.test.order.domain.generic.output.OutputInterface;
import org.test.order.domain.generic.output.OutputStatus;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class ListAllItemsOutput implements OutputInterface {
    private List<ItemEntity> listItems;
    private OutputStatus outputStatus;

    public ListAllItemsOutput(List<ItemEntity> listItems, OutputStatus outputStatus) {
        this.listItems = listItems;
        this.outputStatus = outputStatus;
    }

    @Override
    public Object getBody() {
        return this.listItems;
    }
}
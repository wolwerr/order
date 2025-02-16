package org.test.order.domain.useCase.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.test.order.domain.entity.ItemEntity;
import org.test.order.domain.gateway.item.ListItemIterface;
import org.test.order.domain.generic.output.OutputError;
import org.test.order.domain.generic.output.OutputInterface;
import org.test.order.domain.generic.output.OutputStatus;
import org.test.order.domain.output.Item.ListAllItemsOutput;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ListAllItemsUseCase {
    private final ListItemIterface listItemIterface;
    private OutputInterface listAllItemsOutput;

    public void execute() {
        try {
            List<ItemEntity> listItems = listItemIterface.findListaItens();

            if (listItems == null){
                throw new Exception("Error to list items");
            }
            listAllItemsOutput = new ListAllItemsOutput(
                    listItems,
                    new OutputStatus(200, "OK", "List items successfully")
            );

        } catch (Exception e) {
            listAllItemsOutput = new OutputError(
                    e.getMessage(),
                    new OutputStatus(500, "INTERNAL_SERVER_ERROR", "Error to list items")
            );
        }
    }
}
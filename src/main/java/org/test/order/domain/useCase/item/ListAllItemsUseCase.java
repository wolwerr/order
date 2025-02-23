package org.test.order.domain.useCase.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.order.domain.entity.ItemEntity;
import org.test.order.domain.gateway.cache.CacheInterface;
import org.test.order.domain.gateway.item.ListItemIterface;
import org.test.order.domain.generic.output.OutputError;
import org.test.order.domain.generic.output.OutputInterface;
import org.test.order.domain.generic.output.OutputStatus;
import org.test.order.domain.output.Item.ListAllItemsOutput;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ListAllItemsUseCase {
    private static final Logger logger = LoggerFactory.getLogger(ListAllItemsUseCase.class);
    private final ListItemIterface listItemIterface;
    private final CacheInterface cacheInterface;
    private OutputInterface listAllItemsOutput;

    public void execute() {
        String cacheKey = "allItems";
        List<ItemEntity> cachedItems = (List<ItemEntity>) cacheInterface.getDataFromCache(cacheKey);

        if (cachedItems != null) {
            listAllItemsOutput = new ListAllItemsOutput(
                    cachedItems,
                    new OutputStatus(200, "OK", "List items successfully from cache")
            );
            return;
        }

        try {
            List<ItemEntity> listItems = listItemIterface.findListaItens();
            logger.debug("Fetched items: {}", listItems);

            if (listItems == null || listItems.isEmpty()) {
                throw new Exception("No items found");
            }

            listAllItemsOutput = new ListAllItemsOutput(
                    listItems,
                    new OutputStatus(200, "OK", "List items successfully")
            );

            cacheInterface.cacheData(cacheKey, listItems);

        } catch (Exception e) {
            logger.error("Error listing items", e);
            listAllItemsOutput = new OutputError(
                    e.getMessage(),
                    new OutputStatus(500, "INTERNAL_SERVER_ERROR", "Error to list items")
            );
        }
    }
}
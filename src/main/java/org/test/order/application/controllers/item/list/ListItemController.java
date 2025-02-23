package org.test.order.application.controllers.item.list;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.test.order.application.controllers.item.list.response.ItemResponse;
import org.test.order.application.response.GenericResponse;
import org.test.order.domain.entity.ItemEntity;
import org.test.order.domain.gateway.cache.CacheInterface;
import org.test.order.domain.generic.output.OutputInterface;
import org.test.order.domain.useCase.item.ListAllItemsUseCase;
import org.test.order.infra.adpter.repository.item.ListItemRepository;
import org.test.order.infra.repository.ItemMongoRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ListItemController {
    private final ItemMongoRepository itemMongoRepository;
    private final CacheInterface cacheInterface;
    private final ObjectMapper objectMapper;

    @GetMapping("/list")
    @Operation(summary = "List all items", tags = {"Item"})
    public ResponseEntity<Object> getAllItems() {
        ListAllItemsUseCase listAllItemsUseCase = new ListAllItemsUseCase(new ListItemRepository(itemMongoRepository), cacheInterface);
        listAllItemsUseCase.execute();
        OutputInterface outputInterface = listAllItemsUseCase.getListAllItemsOutput();

        if (outputInterface.getOutputStatus().getCode() == 200) {
            List<ItemResponse> itemResponses = ((List<?>) outputInterface.getBody()).stream()
                    .map(item -> objectMapper.convertValue(item, ItemEntity.class))
                    .map(itemEntity -> new ItemResponse(
                            itemEntity.getUuid(),
                            itemEntity.getName(),
                            itemEntity.getQuantity(),
                            itemEntity.getValue(),
                            itemEntity.getCreatedAt(),
                            itemEntity.getUpdatedAt()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.status(outputInterface.getOutputStatus().getCode()).body(itemResponses);
        }

        return new GenericResponse().response(outputInterface);
    }
}
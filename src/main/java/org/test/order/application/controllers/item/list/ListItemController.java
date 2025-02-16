package org.test.order.application.controllers.item.list;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.test.order.application.response.GenericResponse;
import org.test.order.domain.generic.output.OutputInterface;
import org.test.order.domain.useCase.item.ListAllItemsUseCase;

import org.test.order.infra.adpter.repository.item.ListItemRepository;
import org.test.order.infra.repository.ItemMongoRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ListItemController {
    private final ItemMongoRepository itemMongoRepository;

    @GetMapping("/list")
    @Operation(summary = "List all items", tags = {"Item"})
    public ResponseEntity<Object> getAllItems() {
        ListAllItemsUseCase listAllItemsUseCase = new ListAllItemsUseCase(new ListItemRepository(itemMongoRepository));
        listAllItemsUseCase.execute();
        OutputInterface outputInterface = listAllItemsUseCase.getListAllItemsOutput();

        if (outputInterface.getOutputStatus().getCode() == 200) {
            return new GenericResponse().response(outputInterface);
        }

        return new GenericResponse().response(outputInterface);
    }
}
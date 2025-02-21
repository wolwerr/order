package org.test.order.bdd.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.test.order.infra.collection.item.Item;
import org.test.order.infra.repository.ItemMongoRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RequiredArgsConstructor
public class ListAllItemsSteps {

    private final ItemMongoRepository itemMongoRepository;
    private final RestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    private ResponseEntity<Item[]> response;

    @Before
    public void setup() {
        this.mockServer = MockRestServiceServer.createServer(this.restTemplate);
    }

    @Given("the system has the following items")
    public void theSystemHasTheFollowingItems(List<Map<String, String>> items) throws Exception {
        itemMongoRepository.deleteAll();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        items.forEach(item -> {
            Item itemEntity = new Item(
                    UUID.fromString(item.get("uuid")),
                    item.get("name"),
                    Double.parseDouble(item.get("value")),
                    Integer.parseInt(item.get("quantity")),
                    LocalDateTime.parse(item.get("createdAt"), formatter),
                    LocalDateTime.parse(item.get("updatedAt"), formatter)
            );
            itemMongoRepository.save(itemEntity);
        });
    }

    @When("I request the list of items")
    public void iRequestTheListOfItems() {
        String baseUrl = "http://localhost:8080";
        mockServer.expect(requestTo(baseUrl + "/items/list"))
                .andRespond(withSuccess("[{\"uuid\":\"123e4567-e89b-12d3-a456-426614174000\",\"name\":\"Item 1\",\"quantity\":10,\"value\":100.0,\"createdAt\":\"2023-01-01T00:00:00\",\"updatedAt\":\"2023-01-01T00:00:00\"},{\"uuid\":\"123e4567-e89b-12d3-a456-426614174001\",\"name\":\"Item 2\",\"quantity\":20,\"value\":200.0,\"createdAt\":\"2023-01-02T00:00:00\",\"updatedAt\":\"2023-01-02T00:00:00\"}]", MediaType.valueOf("application/json")));
        response = restTemplate.getForEntity(baseUrl + "/items/list", Item[].class);
    }

    @Then("the response should contain the following items")
    public void theResponseShouldContainTheFollowingItems(List<Map<String, String>> expectedItems) {
        List<Item> actualItems = List.of(Objects.requireNonNull(response.getBody()));
        assertThat(actualItems).hasSize(expectedItems.size());
        for (int i = 0; i < expectedItems.size(); i++) {
            Map<String, String> expectedItem = expectedItems.get(i);
            Item actualItem = actualItems.get(i);
            assertThat(actualItem.getUuid()).isEqualTo(UUID.fromString(expectedItem.get("uuid")));
            assertThat(actualItem.getName()).isEqualTo(expectedItem.get("name"));
            assertThat(actualItem.getQuantity()).isEqualTo(Integer.parseInt(expectedItem.get("quantity")));
            assertThat(actualItem.getValue()).isEqualTo(Double.parseDouble(expectedItem.get("value")));
            assertThat(actualItem.getCreatedAt()).isEqualTo(expectedItem.get("createdAt"));
            assertThat(actualItem.getUpdatedAt()).isEqualTo(expectedItem.get("updatedAt"));
        }
    }
}
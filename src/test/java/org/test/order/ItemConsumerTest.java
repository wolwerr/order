package org.test.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

@SpringBootTest
@DirtiesContext
@ActiveProfiles("test")
public class ItemConsumerTest {

    @Autowired
    private KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    @Test
    public void testSendMultipleMessages() {
        List<Map<String, Object>> messages = List.of(
                Map.of("uuid", "123e4567-e89b-12d3-a456-426614174006", "name", "Item1", "value", 10.0, "quantity", 1, "createdAt", "2023-10-01T10:00:00", "updatedAt", "2023-10-01T10:00:00")
                //Map.of("uuid", "123e4567-e89b-12d3-a456-426614174007", "name", "Item2", "value", 20.0, "quantity", 2, "createdAt", "2023-10-01T11:00:00", "updatedAt", "2023-10-01T11:00:00")
        );

        for (Map<String, Object> message : messages) {
            kafkaTemplate.send("item", message);
        }
    }
}
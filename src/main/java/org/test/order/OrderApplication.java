package org.test.order;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.test.order.infra.kafka.consumers.ItemConsumer;

@SpringBootApplication
public class OrderApplication {

    private final ItemConsumer consumer;

    public OrderApplication(ItemConsumer consumer) {
        this.consumer = consumer;
    }

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @PostConstruct
    public void startConsumer() {
        Thread consumerThread = new Thread(consumer::runConsumer);
        consumerThread.start();
    }

}

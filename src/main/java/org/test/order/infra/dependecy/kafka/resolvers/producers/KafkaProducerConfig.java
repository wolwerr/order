package org.test.order.infra.dependecy.kafka.resolvers.producers;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Properties;

public class KafkaProducerConfig {
    private final KafkaProducer<String, String> producer;
    private final String topic;

    public KafkaProducerConfig(String servers, String topic) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());

        this.producer = new KafkaProducer<>(props);
        this.topic = topic;
    }

    protected void send(String key, String value) {
        System.out.println("Message: " + value);
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception == null) {
                    System.out.println("Message sended with success to topic: " + metadata.topic()
                            + "\nPartition: " + metadata.partition()
                            + "\nOffset: " + metadata.offset()
                            + "\nTimestamp: " + metadata.timestamp());
                } else {
                    System.err.println("Error to send message: " + exception.getMessage());
                }
            }
        });
    }
}
package org.test.order.infra.dependecy.kafka.resolvers.producers;

import org.test.order.infra.kafka.KafkaTopicEnum;

public class KafkaProducerResolver {
    public static String getOrder() {
        return KafkaTopicEnum.order.name();
    }
}

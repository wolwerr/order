package org.test.order.infra.dependecy.kafka.resolvers.consumers;

import org.test.order.infra.kafka.KafkaTopicEnum;

public class KafkaConsumerResolver {
    public static String getItemConsumer(){
        return KafkaTopicEnum.item.name();
    }
}

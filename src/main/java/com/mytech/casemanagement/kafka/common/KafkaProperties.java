package com.mytech.casemanagement.kafka.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:kafkacommonconfig.properties")
public class KafkaProperties {

    @Value("${kafka.bootstrap-servers}")
    String servers;
    @Value("${kafka.topic}")
    String topicName;
    @Value("${kafka.key}")
    String key;

    public String getServers() {
        return servers;
    }

    public String getKey() {
        return key;
    }

    public String getTopicName() {
        return topicName;
    }
}
/*
* use protect modifier can only make sure 'servers' fields be accessed INSIDE of sub-class itself,
* like KafkaConsumerProperties, but it can not be accessed in another class like KafkaConsumerConfig
* so 2 options:
* 1.make the fields public modifier, this solution is to open
* 2.use getter(), so sub class can only get the value, can NOT set the values;
* */


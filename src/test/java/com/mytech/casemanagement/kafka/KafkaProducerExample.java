package com.mytech.casemanagement.kafka;

import java.util.Properties;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.common.errors.RetriableException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
/*
* This is an example from ChatGPT.
* It can send message asynchronously to Apache Kafka (Not Spring Kafka)
* */
@SpringBootTest
public class KafkaProducerExample {

    @Test
    public void sendMessage() {
        // Set up properties
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Your Kafka server address
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        // Create Kafka producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        try {
            // Create a producer record
            ProducerRecord<String, String> record = new ProducerRecord<>("test-topic", "key", "value");

            // Send record asynchronously
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception != null) {
                        exception.printStackTrace();
                    } else {
                        System.out.println("Record sent to partition " + metadata.partition() + " with offset " + metadata.offset());
                    }
                }
            });
        } finally {
            // Close the producer
            producer.close();
        }
    }
}
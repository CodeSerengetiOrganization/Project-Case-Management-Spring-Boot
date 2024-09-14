package com.mytech.casemanagement.kafka.consumer;


import io.swagger.client.model.CaseNew;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListenerCaseNew {

    //todo: to make topics as "${kafka.topic} in common config"
/*    @KafkaListener(topics="test-topic", groupId = "case-consumer-1")
    public void listenerCaseNew(ConsumerRecord<String, CaseNew> record){
        //code to handle the record
        System.out.println("record: "+record.toString());
    }*/

    @KafkaListener(topics="test-topic", groupId = "case-consumer-1")
    public void consume(CaseNew message){
        System.out.println("Received message: " + message);
    }
}

/*
* @Service
public class KafkaConsumer {

    @KafkaListener(topics = "your_topic", groupId = "group_id")
    public void consume(String message) {
        System.out.println("Received message: " + message);
    }
}
*
* */
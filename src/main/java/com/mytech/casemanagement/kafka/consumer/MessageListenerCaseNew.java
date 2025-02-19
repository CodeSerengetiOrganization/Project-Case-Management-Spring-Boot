package com.mytech.casemanagement.kafka.consumer;


import com.mytech.casemanagement.service.CaseServiceNew;
import io.swagger.client.model.CaseNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListenerCaseNew {
    @Autowired
    CaseServiceNew caseServiceNew;

    //todo: to make topics as "${kafka.topic} in common config"
/*    @KafkaListener(topics="test-topic", groupId = "case-consumer-1")
    public void listenerCaseNew(ConsumerRecord<String, CaseNew> record){
        //code to handle the record
        System.out.println("record: "+record.toString());
    }*/

    @KafkaListener(topics="test-topic", groupId = "case-consumer-1")
    public void consume(CaseNew message){
        System.out.println("Received message from Kafka: " + message);
        caseServiceNew.saveCase(caseServiceNew.mapToCaseNewEntity(message));
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
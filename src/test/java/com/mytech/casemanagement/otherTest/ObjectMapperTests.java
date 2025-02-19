package com.mytech.casemanagement.otherTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytech.casemanagement.config.JacksonConfig;
import com.mytech.casemanagement.entity.CaseNew;
import com.mytech.casemanagement.entity.RequestObject;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@ExtendWith(SpringExtension.class)
@SpringJUnitConfig(JacksonConfig.class)
public class ObjectMapperTests {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void shouldMapJsonToRequestObject() throws JsonProcessingException {
        String jsonString="{\n" +
                "  \"action\": \"create\",\n" +
                "  \"payload\": {\n" +
                "    \"caseId\": 1,\n" +
                "    \"caseStatus\": \"PendingDocument\",\n" +
                "    \"caseType\": \"NetNew\",\n" +
                "    \"createdBy\": \"Tony Stark\",\n" +
                "    \"createDate\": \"2024-01-01T12:00:00\",\n" +
                "    \"modifiedDate\": \"2024-01-02T12:00:00\",\n" +
                "    \"pendingReviewDate\": \"2024-01-03T12:00:00\",\n" +
                "    \"note\": \"Good payload in Unit test CaseActionHandlerServiceTests.\"\n" +
                "  }\n" +
                "}";
//        ObjectMapper objectMapper = new ObjectMapper();
        RequestObject requestObject = objectMapper.readValue(jsonString, RequestObject.class);
        System.out.println("requestObject:"+requestObject.toString());
        JsonNode payload = requestObject.getPayload();
        System.out.println("payload:"+payload);
        // Convert JsonNode to CaseNew object
//        objectMapper
        CaseNew caseNew = objectMapper.treeToValue(payload, CaseNew.class);
        System.out.println("caseNew:"+caseNew);
    }

}

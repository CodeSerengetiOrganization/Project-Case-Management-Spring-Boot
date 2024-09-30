package com.mytech.casemanagement.entity;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;

/*@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "action")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CaseNew.class, name = "create")
})*/
public class RequestObject {
    // No additional fields needed, just use polymorphic serialization
    private String action;
    private JsonNode payload;

    // Getter and Setter for action
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    // Getter and Setter for payload
    public JsonNode getPayload() {
        return payload;
    }

    public void setPayload(JsonNode payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "RequestObject{" +
                "action='" + action + '\'' +
                ", payload='" + payload + '\'' +
                '}';
    }
}


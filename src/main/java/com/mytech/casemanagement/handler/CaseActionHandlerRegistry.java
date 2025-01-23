package com.mytech.casemanagement.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CaseActionHandlerRegistry {
    private final Map<String, CaseActionHandler> handlerMap = new HashMap<>();

    @Autowired
    public CaseActionHandlerRegistry(List<CaseActionHandler> handlers) {
        // Register handlers automatically based on the action name
        for (CaseActionHandler handler : handlers) {
            handlerMap.put(handler.getActionName(), handler);
        }
    }

    public CaseActionHandler getHandler(String action) {
        CaseActionHandler handler = handlerMap.get(action);
        if (handler == null) {
            throw new UnsupportedOperationException("No handler found for action: " + action);
        }
        return handler;
    }
}

package com.github.ywj1352.distributed;

import org.springframework.context.ApplicationEvent;

public class DistributedTriggerEvent extends ApplicationEvent {

    /**
     * 触发的methodName;
     */
    private final String methodName;

    public DistributedTriggerEvent(Object source, String methodName) {
        super(source);
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }
}

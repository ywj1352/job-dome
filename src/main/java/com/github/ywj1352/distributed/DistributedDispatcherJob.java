package com.github.ywj1352.distributed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;


/**
 * 分发 当前的被执行的 Job
 */
public class DistributedDispatcherJob implements Runnable {

    @Autowired
    private ApplicationEventPublisher publisher;

    private final String methodName;

    public DistributedDispatcherJob(final String methodName) {
        this.methodName = methodName;
    }

    @Override
    public void run() {
        System.out.println("hahah tetst");
        DistributedTriggerEvent distributedTriggerEvent = new DistributedTriggerEvent(UUID.randomUUID().toString(), methodName);
        publisher.publishEvent(distributedTriggerEvent);
    }
}

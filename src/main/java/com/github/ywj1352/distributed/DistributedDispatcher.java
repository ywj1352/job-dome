package com.github.ywj1352.distributed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class DistributedDispatcher implements ApplicationListener<DistributedTriggerEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DistributedDispatcher.class);

    private final ExecutorService threadPoolExecutor;
    /**
     * key : methodName
     * value : wrapper
     */
    private final Map<String, SampleWrapperObject> map;

    public DistributedDispatcher(final List<SampleWrapperObject> wrapperObjectList,
                                 final ExecutorService threadPoolExecutor) {
        map = new HashMap<>();
        for (SampleWrapperObject wrapperObject : wrapperObjectList) {
            map.put(wrapperObject.getMethodName(), wrapperObject);
        }
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Override
    public void onApplicationEvent(DistributedTriggerEvent event) {
        String taskId = event.getSource().toString();
        LOG.info("trigger the task Method={},TaskID={}", event.getMethodName(), taskId);
        SampleWrapperObject wrapperObject = map.get(event.getMethodName());
        threadPoolExecutor.execute(() -> {
            try {
                wrapperObject.invocation();
            } catch (Throwable tx) {
                LOG.error("execute task Method={},TaskID={} find error", event.getMethodName(), taskId, tx);
            }
        });
    }

}


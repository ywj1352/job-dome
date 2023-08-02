package com.github.ywj1352.config;

import com.github.ywj1352.distributed.DistributedDispatcher;
import com.github.ywj1352.distributed.DistributedDispatcherJob;
import com.github.ywj1352.distributed.DistributedJobProvider;
import com.github.ywj1352.distributed.SampleWrapperObject;
import org.redisson.Redisson;
import org.redisson.RedissonNode;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.RedissonNodeConfig;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Executors;

@Configuration
public class DistributedConfig {

    @Bean(destroyMethod = "shutdown")
    RedissonNode redissonNode(BeanFactory beanFactory) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        /**
         * Config config = new Config();
         *         ClusterServersConfig clusterServersConfig = config.useClusterServers().addNodeAddress("redis://demo.redis.com:6379")
         *                 .setReadMode(ReadMode.MASTER);
         */
        RedissonNodeConfig nodeConfig = new RedissonNodeConfig(config);
        nodeConfig.setExecutorServiceWorkers(Collections.singletonMap(DistributedDispatcherJob.class.getName(), 1));
        nodeConfig.setBeanFactory(beanFactory);
        RedissonNode node = RedissonNode.create(nodeConfig);
        node.start();
        return node;
    }

    @Bean
    RedissonClient redisson() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
/**
 * Config config = new Config();
 *         ClusterServersConfig clusterServersConfig = config.useClusterServers().addNodeAddress("redis://demo.redis.com:6379")
 *                 .setReadMode(ReadMode.MASTER);
 */
        return Redisson.create(config);
    }

    @Bean
    RScheduledExecutorService getExecutorService(RedissonClient redissonClient) {
        RScheduledExecutorService executorService = redissonClient.getExecutorService(DistributedDispatcherJob.class.getName());
        if (executorService.isShutdown()) {
            executorService.delete();
            return redissonClient.getExecutorService(DistributedDispatcherJob.class.getName());
        }
        return executorService;
    }

    @Bean
    DistributedDispatcher distributedDispatcher(DistributedJobProvider provider) {
        Map<String, SampleWrapperObject> allWrapper = provider.getAllWrapper();
        return new DistributedDispatcher(new ArrayList<>(allWrapper.values()), Executors.newFixedThreadPool(10));
    }

}


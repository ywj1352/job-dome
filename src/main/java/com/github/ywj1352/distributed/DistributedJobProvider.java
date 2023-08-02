package com.github.ywj1352.distributed;

import org.redisson.api.CronSchedule;
import org.redisson.api.RScheduledExecutorService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class DistributedJobProvider implements BeanFactoryAware {

    private BeanFactory beanFactory;

    private Map<String, SampleWrapperObject> map = new HashMap<>();


    private final RScheduledExecutorService executorService;

    public DistributedJobProvider(final RScheduledExecutorService executorService) {
        this.executorService = executorService;
    }

    @PostConstruct
    private void findProvider() {
        String basePackage = "com.github.ywj1352.job"; // 指定要扫描的基础包名
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(true);
        scanner.addIncludeFilter(new AnnotationTypeFilter(DistributedScheduled.class));
        Set<BeanDefinition> annotatedBeans = scanner.findCandidateComponents(basePackage);
        for (BeanDefinition beanDefinition : annotatedBeans) {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                System.out.println("Class with @DistributedScheduled annotation: " + clazz.getName());
                findAnnotatedMethods(clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    // 打印带有指定注解的方法
    private void findAnnotatedMethods(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(DistributedScheduled.class)) {
                Object bean = beanFactory.getBean(clazz);
                DistributedScheduled annotation = method.getAnnotation(DistributedScheduled.class);
                SampleWrapperObject sampleWrapperObject = new SampleWrapperObject(method, bean);
                System.out.println("  Method with @DistributedScheduled annotation: " + method.getName());
                map.put(method.getName(), sampleWrapperObject);
                executorService.schedule(new DistributedDispatcherJob(method.getName()), CronSchedule.of(annotation.cron()));
            }
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }


    public Map<String, SampleWrapperObject> getAllWrapper() {
        return map;
    }
}

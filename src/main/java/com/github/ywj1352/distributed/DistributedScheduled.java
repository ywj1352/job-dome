package com.github.ywj1352.distributed;

import java.lang.annotation.*;

/**
 * job can't has
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface DistributedScheduled {


    /**
     *
     * @return
     */
    String cron() default "";


}

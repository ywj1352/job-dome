package com.github.ywj1352.distributed;

import java.lang.reflect.Method;

public class SampleWrapperObject {
    private String methodName;
    private Method method;
    private Object target;

    public SampleWrapperObject(Method method, Object target) {
        this.method = method;
        this.target = target;
        this.methodName = method.getName();
    }

    public Object invocation() {
        try {
            return this.method.invoke(target, null);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

}

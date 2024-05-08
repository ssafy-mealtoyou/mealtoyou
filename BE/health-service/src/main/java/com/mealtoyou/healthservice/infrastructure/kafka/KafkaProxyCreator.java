package com.mealtoyou.healthservice.infrastructure.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class KafkaProxyCreator {
    private final AutowireCapableBeanFactory beanFactory;

    public <T> T createGenericService(Class<T> serviceClass) {
        Constructor<?>[] constructors = serviceClass.getConstructors();
        Constructor<?> constructor = constructors[0];
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] arg = Arrays.stream(parameterTypes)
                .map(beanFactory::getBean)
                .toList()
                .toArray();

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(serviceClass);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            if (method.getDeclaringClass() == Object.class) {
                return proxy.invokeSuper(obj, args);
            }
            if (method.isAnnotationPresent(KafkaMessageListener.class)) {
                try {
                    return proxy.invokeSuper(obj, args);
                } catch (InvocationTargetException e) {
                    throw e.getTargetException();
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Unexpected access exception", e);
                }
            }
            return proxy.invokeSuper(obj, args);
        });

        return (T) enhancer.create(parameterTypes, arg);
    }

}

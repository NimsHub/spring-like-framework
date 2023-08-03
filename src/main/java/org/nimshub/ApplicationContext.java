package org.nimshub;

import org.nimshub.annotations.Autowired;
import org.nimshub.annotations.Component;
import org.nimshub.annotations.ComponentScan;
import org.nimshub.annotations.Configuration;
import org.nimshub.exceptions.ConfigurationNotFound;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ApplicationContext {
    Map<Class<?>, Object> objectContainer = new HashMap<>();

    public ApplicationContext(Class<?> tClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        initializeContext(tClass);
    }

    public <T> T getInstance(Class<T> tClass) throws IllegalAccessException {
        Field[] declaredFields = tClass.getDeclaredFields();
        T instance = (T) objectContainer.get(tClass);
        injectFields(instance, declaredFields);
        return instance;
    }

    private void injectFields(Object instance, Field[] declaredFields) throws IllegalAccessException {
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(Autowired.class)) {
                declaredField.setAccessible(true);
                Class<?> tClass = declaredField.getType();
                Object fieldObject = objectContainer.get(tClass);

                declaredField.set(instance, fieldObject);
                injectFields(fieldObject, tClass.getDeclaredFields());
            }
        }
    }

    private void initializeContext(Class<?> tClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (!tClass.isAnnotationPresent(Configuration.class)) {
            throw new ConfigurationNotFound("Please provide valid configuration field");
        } else {
            ComponentScan componentScan = tClass.getAnnotation(ComponentScan.class);
            String packages = componentScan.value();
            Set<Class<?>> classes = findClasses(packages);
            for (Class<?> pendingClass : classes) {
                if (pendingClass.isAnnotationPresent(Component.class)) {
                    Constructor<?> constructor = pendingClass.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    Object instance = constructor.newInstance();
                    objectContainer.put(pendingClass, instance);
                }
            }
        }
    }

    private Set<Class<?>> findClasses(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet());
    }

    private Class<?> getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}

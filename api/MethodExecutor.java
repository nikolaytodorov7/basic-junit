package api;

import api.annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class MethodExecutor {
    private Map<Method, Integer> beforeAllMethods = new HashMap<>();
    private Map<Method, Integer> beforeEachMethods = new HashMap<>();
    private Map<Method, Integer> testMethods = new HashMap<>();
    private Map<Method, Integer> afterAllMethods = new HashMap<>();
    private Map<Method, Integer> afterEachMethods = new HashMap<>();
    private Class<?> clazz;
    private Object classInstance;


    public MethodExecutor(Class<?> clazz) {
        this.clazz = clazz;
        try {
            classInstance = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("No default constructor in class: " + clazz.getName());
        }
    }

    public void testMethods() throws Exception {
        extractMethods(clazz);
        testMethodsInOrder();
    }

    private void extractMethods(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            int repeatCount = 1;
            RepeatedTest repeatedTest = method.getDeclaredAnnotation(RepeatedTest.class);
            if (repeatedTest != null) {
                validateRepeatedTest(method);
                repeatCount = repeatedTest.value();
            }

            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Test)
                    testMethods.put(method, repeatCount);
                else if (annotation instanceof BeforeAll)
                    beforeAllMethods.put(method, repeatCount);
                else if (annotation instanceof BeforeEach)
                    beforeEachMethods.put(method, repeatCount);
                else if (annotation instanceof AfterAll)
                    afterAllMethods.put(method, repeatCount);
                else if (annotation instanceof AfterEach)
                    afterEachMethods.put(method, repeatCount);
            }
        }
    }

    private void validateRepeatedTest(Method method) {
        Class<?> returnType = method.getReturnType();
        if (!returnType.getName().equals("void"))
            throw new RuntimeException("The return type of the method annotated with @RepeatedTest must be void only!");

        if (Modifier.isStatic(method.getModifiers()))
            throw new RuntimeException("Method annotated with @RepeatedTest cannot be static!");
    }

    private void testMethod(Map.Entry<Method, Integer> entryMethodRepeat) throws InvocationTargetException, IllegalAccessException {
        Method method = entryMethodRepeat.getKey();
        int repeatCount = entryMethodRepeat.getValue();
        for (int i = 0; i < repeatCount; i++) {
            method.invoke(classInstance);
        }
    }

    private void testMethodsInOrder() throws IllegalAccessException, InvocationTargetException {
        for (Map.Entry<Method, Integer> beforeAllMethodsEntry : beforeAllMethods.entrySet()) {
            testMethod(beforeAllMethodsEntry);
        }

        for (Map.Entry<Method, Integer> testMethodsEntry : testMethods.entrySet()) {
            for (Map.Entry<Method, Integer> beforeEachMethodsEntry : beforeEachMethods.entrySet()) {
                testMethod(beforeEachMethodsEntry);
            }

            testMethod(testMethodsEntry);

            for (Map.Entry<Method, Integer> afterEachMethodsEntry : afterEachMethods.entrySet()) {
                testMethod(afterEachMethodsEntry);
            }
        }

        for (Map.Entry<Method, Integer> afterAllMethodsEntry : afterAllMethods.entrySet()) {
            testMethod(afterAllMethodsEntry);
        }
    }
}

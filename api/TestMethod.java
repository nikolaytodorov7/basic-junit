package api;

import api.annotations.*;
import api.asserts.Assertions;
import api.errors.AssertionFailedError;
import api.exceptions.JUnitException;
import api.exceptions.PreconditionViolationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestMethod {
    private int repeatCount = 1;
    private Object classInstance;
    private String executeAnnotationType = null;
    private boolean hasTimeout = false;
    private boolean hasExpected = false;
    private boolean hasDependency = false;
    private long timeout;
    private Class<?> clazz;
    private String[] dependsOnMethods;
    private Class<?> expected;
    private Throwable throwable = null;
    private Set<Method> invokedMethods = new HashSet<>();
    Method method;

    public TestMethod(Method method, Class<?> clazz) throws JUnitException {
        this.clazz = clazz;
        try {
            classInstance = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("No default constructor in class: " + clazz.getName());
        }

        this.method = method;
        processAnnotations();
    }

    public Throwable runTest() throws Exception {
        if (executeAnnotationType == null)
            return null;

        for (int i = 0; i < repeatCount; i++) {
            invokeDependencyMethods();
            if (!hasTimeout) {
                invokeMethod();
                continue;
            }

            Assertions.assertTimeoutPreemptively(Duration.ofMillis(timeout), this::invokeMethod);
        }

        String methodName = method.getName();
        if (throwable != null)
            System.out.printf("|\t+-- %s() [X] %s\n", methodName, throwable.getMessage());
        else
            System.out.printf("|\t+-- %s() [OK]\n", methodName);

        return throwable;
    }

    private void invokeMethod() throws IllegalAccessException, JUnitException {
        Object invoke;
        try {
            invoke = method.invoke(classInstance);
            if (hasExpected && !invoke.equals(expected))
                throw new JUnitException(String.format("Expected '%s' actual '%s'", invoke, expected));

        } catch (InvocationTargetException e) {
            throwable = e.getTargetException();
        }
    }

    private void invokeDependencyMethods() throws Exception {
        if (hasDependency) {
            for (String methodName : dependsOnMethods) {
                Method method = clazz.getDeclaredMethod(methodName);
                if (invokedMethods.contains(method))
                    continue;
                invokedMethods.add(method);
                TestMethod testMethod = new TestMethod(method, clazz);
                testMethod.runTest();
            }
        }
    }

    private void processAnnotations() throws JUnitException {
        RepeatedTest repeatedTestAnnotation = method.getDeclaredAnnotation(RepeatedTest.class);
        if (repeatedTestAnnotation != null) {
            if (repeatedTestAnnotation.value() <= 0) {
                String message = String.format("@RepeatedTest on method [%s %s.%s()] must be declared with a positive 'value'.",
                        method.getReturnType(), clazz.getName(), method.getName());
                throw new IllegalArgumentException(message);
            }

            repeatCount = repeatedTestAnnotation.value();
        }

        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            findExecuteAnnotationType(annotation);
        }
    }

    private void findExecuteAnnotationType(Annotation annotation) throws JUnitException {
        if (annotation instanceof Test testAnnotation)
            processTestAnnotation(testAnnotation);
        else if (annotation instanceof BeforeAll)
            executeAnnotationType = "BeforeAll";
        else if (annotation instanceof BeforeEach)
            executeAnnotationType = "BeforeEach";
        else if (annotation instanceof AfterAll)
            executeAnnotationType = "AfterAll";
        else if (annotation instanceof AfterEach)
            executeAnnotationType = "AfterEach";
        else return;

        if (Modifier.isStatic(method.getModifiers()))
            throw new JUnitException(buildErrorMessage(executeAnnotationType, method));
    }

    private void processTestAnnotation(Test testAnnotation) {
        executeAnnotationType = "Test";
        long timeout = testAnnotation.timeout();
        if (timeout != -1) {
            if (timeout < 1)
                throw new PreconditionViolationException("timeout duration must be a positive number: " + timeout);

            hasTimeout = true;
            this.timeout = timeout;
        }

        String[] dependsOnMethod = testAnnotation.dependsOnMethod();
        if (!Arrays.equals(dependsOnMethod, new String[0])) {
            hasDependency = true;
            this.dependsOnMethods = dependsOnMethod;
        }

        Class<?> expected = testAnnotation.expected();
        if (expected != ExpectedDefaultExceptionClass.class) {
            hasExpected = true;
            this.expected = expected;
        }
    }

    private String buildErrorMessage(String annotation, Method method) {
        return String.format("@%s method '%s' must be static.", annotation, method);
    }
}

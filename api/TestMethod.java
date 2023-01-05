package api;

import api.annotations.*;
import api.exceptions.JUnitException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

public class TestMethod {
    private int repeatCount = 1;
    private Object classInstance;
    Method method;
    String executeAnnotationType = null;
    private boolean hasTimeout = false;
    private boolean hasExpected = false;
    private boolean hasDependency = false;
    private long timeout;
    private Method dependsOnMethod;
    private Class<?> expected;

    public TestMethod(Method method, Object classInstance) throws JUnitException {
        this.classInstance = classInstance;
        this.method = method;

        processAnnotations();
        if (executeAnnotationType == null)
            return;

        processAnnotations();
    }

    public void runMethodTest() throws Exception {
        if (hasTimeout)
            TimeUnit.MILLISECONDS.sleep(timeout);

        for (int i = 0; i < repeatCount; i++) {
            if (hasDependency)
                dependsOnMethod.invoke(classInstance);

            Object invoke = method.invoke(classInstance);
            if (hasExpected && !invoke.equals(expected))
                throw new JUnitException(String.format("Expected '%s' actual '%s'", invoke, expected)); // todo original message check
        }
    }

    private void processAnnotations() throws JUnitException {
        RepeatedTest repeatedTestAnnotation = method.getDeclaredAnnotation(RepeatedTest.class);
        if (repeatedTestAnnotation != null) {
            repeatCount = repeatedTestAnnotation.value();
        }

        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof Test testAnnotation) {
                executeAnnotationType = "Test";
                long timeout = testAnnotation.timeout();
                if (timeout > 0) {
                    hasTimeout = true;
                    this.timeout = timeout;
                }

                Method dependsOnMethod = testAnnotation.dependsOnMethod();
                if (dependsOnMethod != null) {
                    hasDependency = true;
                    this.dependsOnMethod = dependsOnMethod;
                }

                Class<?> expected = testAnnotation.expected();
                if (expected != void.class) {
                    hasExpected = true;
                    this.expected = expected;
                }
            }

            if (annotation instanceof BeforeAll)
                executeAnnotationType = "BeforeAll";
            else if (annotation instanceof BeforeEach)
                executeAnnotationType = "BeforeEach";
            else if (annotation instanceof AfterAll)
                executeAnnotationType = "AfterAll";
            else if (annotation instanceof AfterEach)
                executeAnnotationType = "AfterEach";
            else continue;

            if (Modifier.isStatic(method.getModifiers()))
                throw new JUnitException(buildErrorMessage(executeAnnotationType, method));
        }
    }

    private String buildErrorMessage(String annotation, Method method) {
        return String.format("@%s method '%s' must be static.", annotation, method);
    }
}

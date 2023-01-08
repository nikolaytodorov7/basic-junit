package api;

import api.annotations.*;
import api.exceptions.JUnitException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class MethodExecutor {
    private List<TestMethod> beforeAllMethods = new ArrayList<>();
    private List<TestMethod> beforeEachMethods = new ArrayList<>();
    private List<TestMethod> testMethods = new ArrayList<>();
    private List<TestMethod> afterAllMethods = new ArrayList<>();
    private List<TestMethod> afterEachMethods = new ArrayList<>();
    private List<Throwable> throwableList = new ArrayList<>();
    private Class<?> clazz;

    public MethodExecutor(Class<?> clazz) throws JUnitException {
        this.clazz = clazz;
        extractMethods(clazz);
    }

    public void executeMethods() throws Exception {
        System.out.printf("| +-- %s\n", clazz.getSimpleName());

        for (TestMethod method : beforeAllMethods) {
            executeMethod(method);
        }

        for (TestMethod testMethod : testMethods) {
            for (TestMethod method : beforeEachMethods) {
                executeMethod(method);
            }

            executeMethod(testMethod);

            for (TestMethod method : afterEachMethods) {
                executeMethod(method);
            }
        }

        for (TestMethod method : afterAllMethods) {
            executeMethod(method);
        }

        System.out.printf("Failures(%d):\n", throwableList.size());
        for (Throwable t : throwableList)
            t.printStackTrace();
    }

    private void executeMethod(TestMethod method) throws Exception {
        Throwable throwable = method.runTest();
        if (throwable != null)
            throwableList.add(throwable);
    }

    private void extractMethods(Class<?> clazz) throws JUnitException {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            TestMethod testMethod = new TestMethod(method, clazz);

            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                processAnnotation(testMethod, annotation);
            }
        }
    }

    private void processAnnotation(TestMethod testMethod, Annotation annotation) throws JUnitException {
        if (annotation instanceof Test)
            storeMethod(testMethod, testMethods);
        else if (annotation instanceof BeforeAll)
            storeMethod(testMethod, beforeAllMethods, "BeforeAll");
        else if (annotation instanceof BeforeEach)
            storeMethod(testMethod, beforeEachMethods, "BeforeEach");
        else if (annotation instanceof AfterAll)
            storeMethod(testMethod, afterAllMethods, "AfterAll");
        else if (annotation instanceof AfterEach)
            storeMethod(testMethod, afterEachMethods, "AfterEach");
    }

    private void storeMethod(TestMethod testMethod, List<TestMethod> methodsList) {
        methodsList.add(testMethod);
    }

    private void storeMethod(TestMethod testMethod, List<TestMethod> methodsList, String annotation) throws JUnitException {
        if (!isStaticMethod(testMethod.method))
            throw new JUnitException(buildErrorMessage(annotation, testMethod.method));

        methodsList.add(testMethod);
    }

    private String buildErrorMessage(String annotation, Method method) {
        return String.format("@%s method '%s' must be static.", annotation, method);
    }

    private boolean isStaticMethod(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }
}

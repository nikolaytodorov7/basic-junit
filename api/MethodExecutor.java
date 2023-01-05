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
    private Object classInstance;

    public MethodExecutor(Class<?> clazz) throws JUnitException {
        try {
            classInstance = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("No default constructor in class: " + clazz.getName());
        }

        extractMethods(clazz);
    }

    public void testMethods() throws Exception {
        for (TestMethod method : beforeAllMethods) {
            method.runMethodTest();
        }

        for (TestMethod testMethod : testMethods) {
            for (TestMethod method : beforeEachMethods) {
                method.runMethodTest();
            }

            testMethod.runMethodTest();

            for (TestMethod method : afterEachMethods) {
                method.runMethodTest();
            }
        }

        for (TestMethod method : afterAllMethods) {
            method.runMethodTest();
        }
    }

    private void extractMethods(Class<?> clazz) throws JUnitException {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            TestMethod testMethod = new TestMethod(method, classInstance);

            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
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
        }
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

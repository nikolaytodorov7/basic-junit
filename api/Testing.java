package api;

import api.annotations.*;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class Testing {
    private static List<Method> beforeAllMethods = new ArrayList<>();
    private static List<Method> beforeEachMethods = new ArrayList<>();
    private static List<Method> testMethods = new ArrayList<>();
    private static List<Method> afterAllMethods = new ArrayList<>();
    private static List<Method> afterEachMethods = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        try (URLClassLoader loader = createClassLoader(args[0])) {
            Class<?> clazz = loader.loadClass("test.CupTest");
            extractMethods(clazz);
            Object classInstance = clazz.getDeclaredConstructor().newInstance();
            loadAllMethods(classInstance);
        }
    }

    private static URLClassLoader createClassLoader(String path) throws MalformedURLException {
        File file = new File(path);
        URL[] urls = new URL[]{file.toURI().toURL()};
        return URLClassLoader.newInstance(urls);
    }

    private static void extractMethods(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Test)
                    testMethods.add(method);
                else if (annotation instanceof BeforeAll)
                    beforeAllMethods.add(method);
                else if (annotation instanceof BeforeEach)
                    beforeEachMethods.add(method);
                else if (annotation instanceof AfterAll)
                    afterAllMethods.add(method);
                else if (annotation instanceof AfterEach)
                    afterEachMethods.add(method);
            }
        }
    }

    private static void loadAnnotatedMethods(List<Method> methods, Object classInstance) throws IllegalAccessException, InvocationTargetException {
        for (Method method : methods) {
            method.invoke(classInstance, null);
        }
    }

    private static void loadAllMethods(Object classInstance) throws IllegalAccessException, InvocationTargetException {
        loadAnnotatedMethods(beforeAllMethods, classInstance);
        loadAnnotatedMethods(beforeEachMethods, classInstance);
        loadAnnotatedMethods(testMethods, classInstance);
        loadAnnotatedMethods(afterEachMethods, classInstance);
        loadAnnotatedMethods(afterAllMethods, classInstance);
    }
}

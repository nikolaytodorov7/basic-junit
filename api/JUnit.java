package api;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class JUnit {
    public static void main(String[] args) throws Exception {
        try (URLClassLoader loader = createClassLoader(args[0])) {
            File file = new File(args[0]);
            File[] files = file.listFiles();
            if (files == null || files.length == 0)
                return;

            for (File classFile : files) {
                testClass(loader, classFile);
            }
        }
    }

    private static URLClassLoader createClassLoader(String path) throws MalformedURLException {
        File file = new File(path);
        URL[] urls = new URL[]{file.toURI().toURL()};
        return URLClassLoader.newInstance(urls);
    }

    private static void testClass(URLClassLoader loader, File classFile) throws Exception {
        String fileName = classFile.getName();
        fileName = fileName.split(".java")[0];
        Class<?> clazz = loader.loadClass("test." + fileName);
        MethodExecutor methodExecutor = new MethodExecutor(clazz);
        methodExecutor.testMethods();
    }
}

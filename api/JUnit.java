package api;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class JUnit {
    public static void main(String[] args) throws Exception {
        createLoader(args[0]);
    }

    private static void createLoader(String args) throws Exception {
        try (URLClassLoader loader = createClassLoader(args)) {
            File file = new File(args);
            File[] files = file.listFiles();
            if (files == null || files.length == 0)
                return;

            findTestClasses(loader, files);
        }
    }

    private static void findTestClasses(URLClassLoader loader, File[] files) throws Exception {
        for (File classFile : files) {
            if (classFile.isFile())
                if (classFile.isFile() && classFile.getName().endsWith(".java"))
                    testClass(loader, classFile);

            if (classFile.isDirectory())
                createLoader(classFile.toString());
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
        methodExecutor.executeMethods();
    }
}

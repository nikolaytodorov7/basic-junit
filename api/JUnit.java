package api;

import api.options.ActiveOptions;
import api.options.JUnitOptions;
import org.apache.commons.cli.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class JUnit {
    private static ActiveOptions activeOptions;
    private static CommandLine commandLine;

    public static void main(String[] args) throws Exception {
        parseOptions(args);

        if (activeOptions.selectPackage) {
            String directory = commandLine.getOptionValue(JUnitOptions.SELECT_PACKAGE);
            prepareDirectory(directory);
            return;
        }

        List<String> argList = commandLine.getArgList();
        processClasses(argList);
    }

    private static void parseOptions(String[] args) {
        Options options = JUnitOptions.getOptions();
        CommandLineParser parser = new DefaultParser();
        try {
            commandLine = parser.parse(options, args);
            activeOptions = new ActiveOptions(commandLine);
        } catch (ParseException e) {
            System.out.println("Invalid Options");
        }
    }


    private static void prepareDirectory(String dir) throws Exception {
        try (URLClassLoader loader = createClassLoader(dir)) {
            File file = new File(dir);
            File[] files = file.listFiles();
            if (files == null || files.length == 0)
                return;

            processDirectory(loader, files);
        }
    }

    private static URLClassLoader createClassLoader(String path) throws MalformedURLException {
        File file = new File(path);
        URL[] urls = new URL[]{file.toURI().toURL()};
        return URLClassLoader.newInstance(urls);
    }

    private static void processDirectory(URLClassLoader loader, File[] files) throws Exception {
        for (File classFile : files) {
            if (classFile.isFile() && classFile.getName().endsWith(".class"))
                processClass(loader, classFile);

            if (classFile.isDirectory())
                prepareDirectory(classFile.toString());
        }
    }

    private static void processClasses(List<String> argList) throws Exception {
        for (String clazz : argList) {
            if (!clazz.endsWith(".class"))
                return;

            try (URLClassLoader loader = createClassLoader(clazz)) {
                processClass(loader, new File(clazz));
            }
        }
    }

    private static void processClass(URLClassLoader loader, File classFile) throws Exception {
        String fileName = classFile.getName();
        fileName = fileName.split(".class")[0];
        Class<?> clazz = loader.loadClass(fileName);
        MethodExecutor methodExecutor = new MethodExecutor(clazz);
        methodExecutor.executeMethods();
    }
}

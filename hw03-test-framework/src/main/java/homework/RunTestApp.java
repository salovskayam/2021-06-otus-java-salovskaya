package homework;

import reflection.ReflectionHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RunTestApp {
    private static int passed;
    private static int failed;
    private static int executed;

    public static void runTest(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Method[] methods = clazz.getDeclaredMethods();
            List<Method> methodsBefore = findAnnotatedMethods(methods, Before.class);
            List<Method> methodsTest = findAnnotatedMethods(methods, Test.class);
            List<Method> methodsAfter = findAnnotatedMethods(methods, After.class);

            executed = methodsTest.size();

            for (Method method : methodsTest) {
                Object instance = ReflectionHelper.instantiate(clazz);
                methodsBefore.stream().map(Method::getName)
                        .forEach(x -> ReflectionHelper.callMethod(instance, x));
                try {
                    ReflectionHelper.callMethod(instance, method.getName());
                    passed++;
                } catch (Exception ex) {
                    failed++;
                }
                methodsAfter.stream().map(Method::getName)
                        .forEach(x -> ReflectionHelper.callMethod(instance, x));
            }

            printResult();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static List<Method> findAnnotatedMethods(Method[] methods, Class<? extends Annotation> annotationClass) {
        return Arrays.stream(methods).filter(x -> x.isAnnotationPresent(annotationClass))
                .collect(Collectors.toList());
    }

    private static void printResult() {
        System.out.println("executed: " + executed + "\n"
                + "passed: " + passed + "\n"
                + "failed: " + failed + "\n");
    }
}

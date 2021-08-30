package ru.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class TestLoggingProxy {

    private TestLoggingProxy() {
    }

    static Calculation createTestLoggingProxy() {
        InvocationHandler handler = new TestLoggingInvocationHandler(new TestLogging());

        return (Calculation) Proxy.newProxyInstance(Calculation.class.getClassLoader(),
                new Class<?>[]{Calculation.class}, handler);
    }

    static class TestLoggingInvocationHandler implements InvocationHandler {
        private final Calculation calculation;
        private final List<Method> annotatedMethods;

        TestLoggingInvocationHandler(Calculation calculation) {
            this.calculation = calculation;
            this.annotatedMethods = getAnnotatedMethods(calculation, Log.class);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (methodIsEqual(annotatedMethods, method)) {
                printResult(method, args);
            }
            return method.invoke(calculation, args);
        }

        private static List<Method> getAnnotatedMethods(Object obj, Class<? extends Annotation> annotationClass) {
            return Arrays.stream(obj.getClass().getDeclaredMethods()).filter(x -> x.isAnnotationPresent(annotationClass))
                    .collect(Collectors.toList());
        }

        private static boolean methodIsEqual(List<Method> annotatedMethods, Method method) {
            for (Method m : annotatedMethods) {
                if (!m.getName().equals(method.getName())) {
                    continue;
                }
                if (!Arrays.equals(m.getParameterTypes(), method.getParameterTypes())) {
                    continue;
                }
                if (m.getReturnType().equals(method.getReturnType())) {
                    return true;
                }
            }
            return false;
        }
    }

    private static void printResult(Method method, Object[] args) {
        StringBuilder builder = new StringBuilder();
        builder.append("executed method: ")
                .append(method.getName())
                .append(", param: ");
        if (args != null) {
            builder.append(getParameters(args));
        }
        System.out.println(builder);
    }

    private static String getParameters(Object[] args) {
        return Arrays.stream(args).map(Object::toString).collect(Collectors.joining(", "));
    }

}
package ru.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
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

        TestLoggingInvocationHandler(Calculation calculation) {
            this.calculation = calculation;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (methodIsAnnotated(calculation, method, Log.class)) {
                printResult(method, args);
            }
            return method.invoke(calculation, args);
        }
    }

    private static boolean methodIsAnnotated(Object obj, Method method, Class<? extends Annotation> annotationClass) {
        for (Method m : obj.getClass().getDeclaredMethods()) {
            if (!m.getName().equals(method.getName())) {
                continue;
            }
            if (!Arrays.equals(m.getParameterTypes(), method.getParameterTypes())) {
                continue;
            }
            if (!m.getReturnType().equals(method.getReturnType())) {
                continue;
            }
            if (m.isAnnotationPresent(annotationClass)) {
                return true;
            }
        }
        return false;
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
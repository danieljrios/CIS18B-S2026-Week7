package edu.norcocollege.cis18b.week7.mini07;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class VirtualThreadSupport {
    private static final String ERROR_MESSAGE = "Virtual threads require Java 21 or newer.";

    private VirtualThreadSupport() {
    }

    public static boolean isAvailable() {
        try {
            // Java 21 has Executors.newVirtualThreadPerTaskExecutor().
            // Reflection lets this project still compile on Java 17.
            Executors.class.getMethod("newVirtualThreadPerTaskExecutor");
            return true;
        } catch (NoSuchMethodException ex) {
            return false;
        }
    }

    public static ExecutorService newExecutor() {
        if (!isAvailable()) {
            throw new IllegalStateException(ERROR_MESSAGE);
        }

        try {
            // Call Executors.newVirtualThreadPerTaskExecutor() only when it exists.
            Method factory = Executors.class.getMethod("newVirtualThreadPerTaskExecutor");
            return (ExecutorService) factory.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            throw new IllegalStateException(ERROR_MESSAGE, ex);
        }
    }

    public static String errorMessage() {
        return ERROR_MESSAGE;
    }
}
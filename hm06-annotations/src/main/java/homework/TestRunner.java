package homework;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    public static void runTests(Class<?> testClass) {
        List<Method> beforeMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();
        List<Method> afterMethods = new ArrayList<>();
        categorizeMethods(testClass, beforeMethods, testMethods, afterMethods);

        int totalTests = testMethods.size();
        int passedTests = 0;
        int failedTests = 0;

        for (Method testMethod : testMethods) {
            try {
                Object testInstance = testClass.getDeclaredConstructor().newInstance();

                boolean beforeSuccess = executeMethods(beforeMethods, testInstance);

                if (beforeSuccess) {
                    try {
                        testMethod.invoke(testInstance);
                        passedTests++;
                    } catch (Exception e) {
                        System.out.println("Test failed: " + testMethod.getName());
                        failedTests++;
                    }
                } else {
                    failedTests++;
                }

                executeMethods(afterMethods, testInstance);

            } catch (Exception e) {
                System.out.println("Error running test: " + testMethod.getName());
                failedTests++;
            }
        }

        printTestSummary(totalTests, passedTests, failedTests);
    }

    private static void categorizeMethods(Class<?> testClass, List<Method> beforeMethods, List<Method> testMethods, List<Method> afterMethods) {
        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                beforeMethods.add(method);
            } else if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            } else if (method.isAnnotationPresent(After.class)) {
                afterMethods.add(method);
            }
        }
    }

    private static boolean executeMethods(List<Method> methods, Object instance) {
        for (Method method : methods) {
            try {
                method.invoke(instance);
            } catch (Exception e) {
                System.out.println("Error executing method: " + method.getName());
                return false;
            }
        }
        return true;
    }

    private static void printTestSummary(int totalTests, int passedTests, int failedTests) {
        System.out.println("Total tests: " + totalTests);
        System.out.println("Passed tests: " + passedTests);
        System.out.println("Failed tests: " + failedTests);
    }
}

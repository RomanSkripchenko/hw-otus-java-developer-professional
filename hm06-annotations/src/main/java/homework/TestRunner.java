import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide the test class name");
            return;
        }

        String testClassName = args[0];

        try {
            Class<?> testClass = Class.forName(testClassName);
            runTests(testClass);
        } catch (ClassNotFoundException e) {
            System.out.println("Test class not found: " + testClassName);
        }
    }

    private static void runTests(Class<?> testClass) {
        Method[] methods = testClass.getDeclaredMethods();
        List<Method> beforeMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();
        List<Method> afterMethods = new ArrayList<>();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Before.class)) {
                beforeMethods.add(method);
            } else if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            } else if (method.isAnnotationPresent(After.class)) {
                afterMethods.add(method);
            }
        }

        int totalTests = testMethods.size();
        int passedTests = 0;
        int failedTests = 0;

        for (Method testMethod : testMethods) {
            try {
                Object testInstance = testClass.getDeclaredConstructor().newInstance();

                for (Method beforeMethod : beforeMethods) {
                    beforeMethod.invoke(testInstance);
                }

                try {
                    testMethod.invoke(testInstance);
                    passedTests++;
                } catch (Exception e) {
                    System.out.println("Test failed: " + testMethod.getName());
                    failedTests++;
                }

                for (Method afterMethod : afterMethods) {
                    afterMethod.invoke(testInstance);
                }

            } catch (Exception e) {
                System.out.println("Error running test: " + testMethod.getName());
                failedTests++;
            }
        }

        System.out.println("Total tests: " + totalTests);
        System.out.println("Passed tests: " + passedTests);
        System.out.println("Failed tests: " + failedTests);
    }
}

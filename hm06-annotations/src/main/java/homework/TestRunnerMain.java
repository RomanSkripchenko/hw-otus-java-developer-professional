package homework;

public class TestRunnerMain {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide the test class name");
            return;
        }

        String testClassName = args[0];

        try {
            Class<?> testClass = Class.forName(testClassName);
            TestRunner.runTests(testClass);
        } catch (ClassNotFoundException e) {
            System.out.println("Test class not found: " + testClassName);
        }
    }
}

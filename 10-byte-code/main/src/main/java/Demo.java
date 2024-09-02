public class Demo {
    public static void main(String[] args) {
        TestLoggingInterface testLogging =
                LogInvocationHandler.createProxy(new TestLogging(), TestLoggingInterface.class);
        testLogging.calculation(6);
        testLogging.calculation(2, 3);
        testLogging.calculation(1, 4, "example");
    }
}

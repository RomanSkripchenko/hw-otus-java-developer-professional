public class MyTestClass {

    @Before
    public void setup() {
        System.out.println("Setting up before test");
    }

    @Test
    public void test1() {
        System.out.println("Running test1");
    }

    @Test
    public void test2() {
        System.out.println("Running test2");
        throw new RuntimeException("Test2 failed");
    }

    @After
    public void teardown() {
        System.out.println("Tearing down after test");
    }
}

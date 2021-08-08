package homework;

public class TestClass {
    @Before
    void runBeforeOneTest() {
        System.out.println("run before each test one");
    }

    @Before
    void runBeforeTwoTest() {
        System.out.println("run before each test two");
    }

    @Test
    void runTestOne() {
        System.out.println("run test 1");
    }

    @Test
    void runTestTwo() {
        System.out.println("run test 2");
        throw new RuntimeException();
    }

    @After
    void runAfterTest() {
        System.out.println("run after each test");
        System.out.println("---------------------");
    }
}

public class CardUnittest {

    public static void main(String[] args) {
        testConstructor();
        testGetCardNumber();
        testToStringMethod();
    }

    private static void test(String testName, boolean condition) {
        if (condition) {
            System.out.println("[OK] " + testName);
        } else {
            System.out.println("[FAIL] " + testName);
        }
    }

    private static void testConstructor() {
        Card card = new Card("1234-5678-9012-3456");
        test("Constructor test", card.getCardNumber().equals("1234-5678-9012-3456"));
    }

    private static void testGetCardNumber() {
        Card card = new Card("9999-0000-1111-2222");
        test("getCardNumber method", card.getCardNumber().equals("9999-0000-1111-2222"));
    }

    private static void testToStringMethod() {
        Card card = new Card("5555-6666-7777-8888");
        test("toString method", card.toString().equals("5555-6666-7777-8888"));
    }
}

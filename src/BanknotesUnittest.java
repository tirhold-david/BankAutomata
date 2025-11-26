public class BanknotesUnittest {

    public static void main(String[] args) {
        testConstructor();
        testChangeCountPositive();
        testChangeCountNegative();
        testChangeCountTooLarge();
        testChangeCountTooSmall();
        testChangeCountZero();
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
        Banknotes b = new Banknotes(1000, 50);
        test("Constructor test (denomination)", b.getDenomination() == 1000);
        test("Constructor test (count)", b.getCount() == 50);
    }

    private static void testChangeCountPositive() {
        try {
            Banknotes b = new Banknotes(100, 50);
            b.changeCount(20); // 50 + 20 = 70
            test("changeCount positive", b.getCount() == 70);
        } catch (WrongAmountException e) {
            test("changeCount positive (exception)", false);
        }
    }

    private static void testChangeCountNegative() {
        try {
            Banknotes b = new Banknotes(100, 50);
            b.changeCount(-10); // 50 - 10 = 40
            test("changeCount negative", b.getCount() == 40);
        } catch (WrongAmountException e) {
            test("changeCount negative (exception)", false);
        }
    }

    private static void testChangeCountTooLarge() {
        try {
            Banknotes b = new Banknotes(100, 50);
            b.changeCount(100); // túl nagy → exception kell
            test("changeCount too large (no exception)", false);
        } catch (WrongAmountException e) {
            test("changeCount too large", true);
        }
    }

    private static void testChangeCountTooSmall() {
        try {
            Banknotes b = new Banknotes(100, 50);
            b.changeCount(-100); // túl kicsi → exception kell
            test("changeCount too small (no exception)", false);
        } catch (WrongAmountException e) {
            test("changeCount too small", true);
        }
    }

    private static void testChangeCountZero() {
        try {
            Banknotes b = new Banknotes(100, 50);
            b.changeCount(0); // nem engedélyezett → exception kell
            test("changeCount zero (no exception)", false);
        } catch (WrongAmountException e) {
            test("changeCount zero", true);
        }
    }

    private static void testToStringMethod() {
        Banknotes b = new Banknotes(1000, 50);
        test("toString method", b.toString().equals("1000 Ft: 50 db"));
    }
}

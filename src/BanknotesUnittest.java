public class BanknotesUnittest {
    public static void main(String[] args) {
        testToString();
    }

    public static void testToString() {
        Banknotes note = new Banknotes(1000, 100);
        if (!note.toString().equals("1000 Ft: 100 db")) {
            System.out.println("Hiba a setter metódusokban!");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        BanknoteStorage storage = new BanknoteStorage();
        storage.printStorageStatus();

        System.out.println("\nHozzáadunk 10 db 2000 Ft-ost...");
        try {
            storage.addBanknotes(2000, 10);
        } catch (WrongAmountException e) {
            System.out.println("Hiba a mennyiséggel: " + e.getMessage());
        }

        storage.printStorageStatus();
    }
}
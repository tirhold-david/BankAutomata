public class Main {
    public static void main(String[] args) {
        BanknoteStorage storage = new BanknoteStorage();
        storage.printStorageStatus();

        System.out.println("\nHozzáadunk 10 db 2000 Ft-ost...");
        storage.addBanknotes(2000, 10);

        storage.printStorageStatus();
    }
}
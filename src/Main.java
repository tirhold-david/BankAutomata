public class Main {
    public static void main(String[] args) {
        BanknoteStorage storage = new BanknoteStorage();

        storage.printStorageStatus();

        System.out.println("\nHozzáadunk 5 db 2000 Ft-ost:");
        storage.addBanknotes(2000, 5);

        storage.printStorageStatus();

        System.out.println("\nA 10000 Ft-osok száma: " + storage.getCountForDenomination(10000));
    }
}

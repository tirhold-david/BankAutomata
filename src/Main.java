import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BanknoteStorage storage = new BanknoteStorage();


        //kiírás teszt jelleggel
        storage.printStorageStatus();
        System.out.println("\nEgyes címletek:");
        System.out.println("20000 Ft: " + storage.getCountForDenomination(20000) + " db");
        System.out.println("1000 Ft: " + storage.getCountForDenomination(1000) + " db");

        //bankjegy hozzáadása
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nCímlet: ");
        int denomination = scanner.nextInt();
        System.out.print("Mennyiség: ");
        int amount = scanner.nextInt();

        boolean success = storage.addBanknotes(denomination, amount);
        if (success) {
            System.out.println("Sikeresen hozzáadva!");
        } else {
            System.out.println("Hiba: érvénytelen adatok!");
        }
        storage.printStorageStatus(); //ellenőrzésnek megint kiírjuk
    }
}

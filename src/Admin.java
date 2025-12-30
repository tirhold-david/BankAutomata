import java.util.Scanner;

public class Admin implements iBankAutomata {
    private static final Scanner scanner = ATMManager.getScanner();


    public static void adminMode() {
        System.out.print("Admin jelszó: ");
        String password = scanner.nextLine().trim();

        if (PASSWORD.equals(password)) {
            System.out.println("\n✓ Sikeres admin belépés!");
            adminMenu();
        } else {
            System.out.println("Hibás admin jelszó!");
        }
    }

    private static void adminMenu() {
        System.out.println("\nAdmin menü:\n1. Bankjegy készlet megtekintése\n2. Bankjegy feltöltése\n3. Kártya feloldása feketelistáról\n4. Vissza a főmenühöz");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());

            switch (choice) {
                case 1:
                    showStock();
                    adminMenu();
                    break;
                case 2:
                    refillStock();
                    adminMenu();
                    break;
                case 3:
                    unlockCard();
                    adminMenu();
                    break;
                case 4:
                    ATMManager.welcome();
                    break;
                default:
                    throw new InvalidInputException("Ilyen opció nem létezik!");
            }
        } catch (NumberFormatException nfe) {
            System.out.println("Csak számot adjon meg!");
            adminMenu();
        } catch (InvalidInputException iie) {
            System.out.println(iie.getMessage());
            adminMenu();
        }
    }

    private static void showStock() {
        System.out.println("\nJelenlegi bankjegy készlet:");
        for (Banknotes note : ATMManager.getNotes()) {
            System.out.println(note.getDenomination() + " Ft: " + note.getCount() + " db");
        }
        System.out.println();
    }

    private static void refillStock() {
        System.out.println("\nBankjegy feltöltés:");
        for (int i = 0; i < ATMManager.getNotes().size(); i++) {
            Banknotes note = ATMManager.getNotes().get(i);
            System.out.print(note.getDenomination() + " Ft-os bankjegyek száma: ");
            try {
                note.changeCount(100 - note.getCount());
                System.out.println("✓ Frissítve!");
            } catch (WrongAmountException wae) {
                System.out.println(wae.getMessage());
            }
        }
        try {
            ATMManager.saveNotesToFile();
            System.out.println("✓ Minden készlet frissítve és mentve!");
        } catch (InvalidInputException iie) {
            System.out.println(iie.getMessage());
        }
    }

    private static void unlockCard() {
        System.out.print("\nFeloldani kívánt számlaszám: ");
        String cardNumber = scanner.nextLine().trim();

        Card card = new Card(cardNumber);

        try {
            if (!cardNumber.matches("\\w{4} \\w{4} \\w{4} \\w{4}")) {
                throw new InvalidInputException("Hibás kártyaszám formátum!");
            }

            if (!BlackListManager.isBlacklisted(card)) {
                throw new InvalidInputException("Ez a kártya nincs a feketelistán!");
            }
        } catch (InvalidInputException iie) {
            System.out.println(iie.getMessage());
            adminMenu();
        }

        BlackListManager.removeCard(card);

        System.out.println("A kártya sikeresen feloldva!");
    }

}

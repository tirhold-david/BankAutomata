import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ATMManager {
    private static ArrayList<Banknotes> notes = new ArrayList<>();

    public ATMManager() {
        try (BufferedReader reader = new BufferedReader(new FileReader("NoteSave.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, ";");
                int denomination = Integer.parseInt(tokenizer.nextToken().trim());
                int count = Integer.parseInt(tokenizer.nextToken().trim());
                notes.add(new Banknotes(denomination, count));
            }
        } catch (FileNotFoundException e) {
            System.out.println("A fájl nem található! Hiba: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Hiba a fájl olvasása közben: " + e.getMessage());
        }
    }

    // Visszaadja a címleteket int[] formában
    public static int[] getDenominations() {
        int[] result = new int[notes.size()];
        for (int i = 0; i < notes.size(); i++) {
            result[i] = notes.get(i).getDenomination();
        }
        return result;
    }

    // Visszaadja a darabszámokat int[] formában
    public static int[] getCounts() {
        int[] result = new int[notes.size()];
        for (int i = 0; i < notes.size(); i++) {
            result[i] = notes.get(i).getCount();
        }
        return result;
    }

    // Adott címlethez tartozó darabszám lekérdezése
    public static int getCountForDenomination(int denomination) {
        for (Banknotes note : notes) {
            if (note.getDenomination() == denomination) {
                return note.getCount();
            }
        }
        return -1; // nem található
    }

    // Készlet kiíratása
    public static void printStorageStatus() {
        System.out.println("Bankjegy készlet:");
        for (Banknotes note : notes) {
            System.out.println(note);
        }
    }

    // Bankjegyek hozzáadása
    public static void changeBanknotesAmount(int denomination, int amount) throws WrongAmountException, InvalidDenominationException {
        for (Banknotes note : notes) {
            if (note.getDenomination() == denomination) {
                try {
                    note.changeCount(amount);
                } catch (WrongAmountException wae) {
                    throw new WrongAmountException(wae.getMessage());
                }
            }
        }

        throw new InvalidDenominationException("Nem létezik ilyen bankjegy!");
    }

    public static void welcome() {
        System.out.println("\nÜdvözöllek a Tirhold ATM termináljánál!\n\nRendelkezésre álló műveletek:\n1. Pénzlevétel\n" +
                "2. Admin mód\n3. Kilépés\n(A kiválasztott opció számát írja be!)");
        Scanner scanner = new Scanner(System.in);

        int command = scanner.nextInt();

        try {
            switch (command) {
                case 1:
                    withdraw();
                    break;
                case 2:
                    // admin
                    break;
                case 3:
                    System.out.println("Viszont látásra!");
                    break;
                default:
                    throw new InvalidInputException("Ilyen opció nem létezik!");
            }
        } catch (InvalidInputException iie) {
            System.out.println("Hiba a bemenetben: " + iie.getMessage());
            scanner.reset();
            welcome();
        }
    }

    public static void withdraw() throws InvalidInputException {
        Scanner scanner = new Scanner(System.in);

        try {
            if (checkPin(checkCardNumber(scanner), scanner)) {
                System.out.println("Helyes");
            } else {
                throw new WrongPinException("\nHibás PIN kód! A kártyát a rendszer letiltotta!");
            }

        } catch (InvalidInputException iie) {
            System.out.println(iie.getMessage());
            welcome();
        } catch (WrongPinException wpe) {
            System.out.println(wpe.getMessage());
            // blacklist
        }
        catch (NumberFormatException nfe) {
            System.out.println("A kártyaszám és a PIN kód csak számokat tartalmazhat!");
            welcome();
        }
    }

    private static String checkCardNumber(Scanner scanner) throws InvalidInputException {
        System.out.print("Írja be a kártyaszámot: ");

        String cardNum = scanner.nextLine().trim();

        StringTokenizer tokenizer = new StringTokenizer(cardNum, " ");

        if (tokenizer.countTokens() != 4) {
            throw new InvalidInputException("Hibás kártyaszám formátum! A kártyaszám 4x4 számjegy szóközzel elválasztva.");
        } else {
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                if (!token.matches("[0-9]{4}")) {
                    throw new InvalidInputException("Hibás kártyaszám formátum! A kártyaszám 4x4 számjegy szóközzel elválasztva.");
                }
            }
        }

        return cardNum.split(" ")[0];
    }

    private static boolean checkPin(String cardNumber, Scanner scanner) throws WrongPinException {
        int chances = 3;
        int pin, reverseCard;

        do {
            try {
                if (chances < 3) {
                    throw new WrongPinException("Hibás PIN kód!");
                }
            } catch (WrongPinException wpi) {
                System.out.println(wpi.getMessage());
            }

            chances--;

            System.out.print("Írja be a PIN kódot: ");

            pin = Integer.parseInt(scanner.nextLine().trim());

            StringBuilder stringBuilder = new StringBuilder(cardNumber);
            reverseCard = Integer.parseInt(stringBuilder.reverse().toString());

        } while (chances > 0 && reverseCard != pin);

        return reverseCard == pin;
    }
}
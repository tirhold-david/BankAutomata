import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.StringTokenizer;

public class BanknoteStorage {
    private static ArrayList<Banknotes> notes = new ArrayList<>();

    public BanknoteStorage() {
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
                "2. Admin mód\n3. Kilépés\n(A kiválasztott opció sorszámát írd be!)");
        Scanner scanner = new Scanner(System.in);

        int command = scanner.nextInt();

        try {
            switch (command) {
                case 1:
                    // felvétel
                    break;
                case 2:
                    // admin
                    break;
                case 3:
                    System.out.println("Viszont látásra!");
                    scanner.close();
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
}
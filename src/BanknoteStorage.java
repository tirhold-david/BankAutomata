import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

public class BanknoteStorage {
    private ArrayList<Banknotes> notes = new ArrayList<>();

    public BanknoteStorage() {
        try (BufferedReader reader = new BufferedReader(new FileReader("NoteSave.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, ";");
                if (tokenizer.countTokens() == 2) {
                    try {
                        int denomination = Integer.parseInt(tokenizer.nextToken().trim());
                        int count = Integer.parseInt(tokenizer.nextToken().trim());
                        notes.add(new Banknotes(denomination, count));
                    } catch (NumberFormatException e) {
                        System.out.println("Hibás adat a sorban: " + line);
                        System.out.println("Hiba: " + e.getMessage());
                    }
                } else {
                    System.out.println("Hibás formátum: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("A fájl nem található! Hiba: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Hiba a fájl olvasása közben: " + e.getMessage());
        }
    }

    // Visszaadja a címleteket int[] formában
    public int[] getDenominations() {
        int[] result = new int[notes.size()];
        for (int i = 0; i < notes.size(); i++) {
            result[i] = notes.get(i).getDenomination();
        }
        return result;
    }

    // Visszaadja a darabszámokat int[] formában
    public int[] getCounts() {
        int[] result = new int[notes.size()];
        for (int i = 0; i < notes.size(); i++) {
            result[i] = notes.get(i).getCount();
        }
        return result;
    }

    // Adott címlethez tartozó darabszám lekérdezése
    public int getCountForDenomination(int denomination) {
        for (Banknotes note : notes) {
            if (note.getDenomination() == denomination) {
                return note.getCount();
            }
        }
        return -1; // nem található
    }

    // Készlet kiíratása
    public void printStorageStatus() {
        System.out.println("Bankjegy készlet:");
        for (Banknotes note : notes) {
            System.out.println(note);
        }
    }

    // Bankjegyek hozzáadása
    public boolean addBanknotes(int denomination, int amount) {
        if (amount > 0) {
            for (Banknotes note : notes) {
                if (note.getDenomination() == denomination) {
                    note.changeCount(amount);
                    return true;
                }
            }
        }
        return false;
    }
}
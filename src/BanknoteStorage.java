import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class BanknoteStorage {
    private ArrayList<Banknotes> notes = new ArrayList<>();

    public BanknoteStorage() throws FileNotFoundException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("NoteSave.txt"));

            for (String line : reader.lines().toList()) {
                StringTokenizer tokenizer = new StringTokenizer(line, ";");

                Banknotes note = new Banknotes(Integer.parseInt(tokenizer.nextToken()), Integer.parseInt(tokenizer.nextToken()));
                notes.add(note);
            }
        } catch (FileNotFoundException e) {
            System.out.println("A fájl nem található! Hiba: " + e.getMessage());
        }
    }

    public int[] getDenominations() {
        return denominations.clone();
    }

    public int[] getCounts() {
        return counts.clone();
    }

    public int getCountForDenomination(int denomination) {
        for (int i = 0; i < denominations.length; i++) {
            if (denominations[i] == denomination) {
                return counts[i];
            }
        }
        return -1; //nem található címlet
    }

    public void printStorageStatus() {
        System.out.println("Bankjegy készlet:");
        for (int i = 0; i < denominations.length; i++) {
            System.out.println(denominations[i] + " Ft: " + counts[i] + " db");
        }
    }

    public boolean addBanknotes(int denomination, int amount) {
        // 1. Validáljuk a címletet
        for (int i = 0; i < denominations.length; i++) {
            if (denominations[i] == denomination) {
                // 2. Validáljuk a mennyiséget
                if (amount > 0) {
                    // 3. Növeljük a készletet
                    counts[i] += amount;
                    return true; // sikeres
                }
            }
        }
        return false; // sikertelen
    }
}
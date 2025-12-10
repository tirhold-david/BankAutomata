import java.io.*;
import java.util.HashSet;

public class BlackListManager {
    private static final String FILE_NAME = "BlackList.txt";
    private static HashSet<String> blacklistedCards = new HashSet<>();

    // Feketelista beolvasása
    public static void load() {
        blacklistedCards.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                blacklistedCards.add(line.trim());
            }
        } catch (FileNotFoundException e) {
            System.out.println("A feketelista fájl nem található, új fájlt hozunk létre.");
        } catch (IOException e) {
            System.out.println("Hiba a feketelista olvasása közben: " + e.getMessage());
        }
    }

    // Mentés fájlba
    public static void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String card : blacklistedCards) {
                bw.write(card);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Hiba a feketelista mentése közben: " + e.getMessage());
        }
    }

    // Hozzáadás feketelistához
    public static void addCard(Card card) {
        blacklistedCards.add(card.getCardNumber());
        save();
    }

    // Ellenőrzés
    public static boolean isBlacklisted(Card card) {
        return blacklistedCards.contains(card.getCardNumber());
    }
}

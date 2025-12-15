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
        BlackListManager.load();

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

    public static void welcome() {
        System.out.println("\nÜdvözöllek a Tirhold ATM termináljánál!\n\nRendelkezésre álló műveletek:\n1. Pénzlevétel\n" +
                "2. Admin mód\n3. Kilépés\n(A kiválasztott opció számát írja be!)");

        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine().trim();
        int command;

        try {
            command = Integer.parseInt(input);

            switch (command) {
                case 1:
                    withdraw();
                    break;
                case 2:
                    // admin mód
                    break;
                case 3:
                    System.out.println("Viszont látásra!");
                    break;
                default:
                    throw new InvalidInputException("Ilyen opció nem létezik!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Csak számot adjon meg!");
            welcome();
        } catch (InvalidInputException iie) {
            System.out.println(iie.getMessage());
            welcome();
        }
    }

    public static void withdraw() {
        Scanner scanner = new Scanner(System.in);

        try {
            Card card = checkCardNumber(scanner);

            if (checkPin(card, scanner)) {
                System.out.println("Mennyi pénzt szeretne felvenni?");
                dispenseCash(scanner.nextInt());
            }

        } catch (InvalidInputException iie) {
            System.out.println(iie.getMessage());
            welcome();
        } catch (WrongPinException wpe) {
            System.out.println(wpe.getMessage());
            welcome();
        } catch (NumberFormatException nfe) {
            System.out.println("A kártyaszám és a PIN csak számot tartalmazhat!");
            welcome();
        } catch (WrongAmountException wae) {
            System.out.println(wae.getMessage());
            welcome();
        }
    }

    private static Card checkCardNumber(Scanner scanner) throws InvalidInputException {
        System.out.print("Írja be a kártyaszámot (XXXX XXXX XXXX XXXX): ");
        String cardNum = scanner.nextLine().trim();

        if (cardNum.isEmpty()) {
            throw new InvalidInputException("A kártyaszám nem lehet üres!");
        }

        StringTokenizer tokenizer = new StringTokenizer(cardNum, " ");

        if (tokenizer.countTokens() != 4) {
            throw new InvalidInputException("A kártyaszám formátuma hibás: XXXX XXXX XXXX XXXX");
        }

        StringBuilder full = new StringBuilder();

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (!token.matches("[0-9]{4}")) {
                throw new InvalidInputException("A kártyaszám csak számokat tartalmazhat!");
            }
            full.append(token);
        }

        Card card = new Card(full.toString());

        if (BlackListManager.isBlacklisted(card)) {
            throw new InvalidInputException("Ez a kártya tiltva van!");
        }

        return card;
    }

    private static boolean checkPin(Card card, Scanner scanner) throws WrongPinException {
        int chances = 3;

        String reversedCard = new StringBuilder(card.getCardNumber().substring(0, 4)).reverse().toString();

        while (chances > 0) {

            if (chances < 3) {
                System.out.println("Hibás PIN kód!");
            }

            System.out.print("Írja be a PIN kódot: ");
            String pin = scanner.nextLine().trim();

            if (!pin.matches("[0-9]+")) {
                throw new WrongPinException("A PIN csak számjegyeket tartalmazhat!");
            }

            if (pin.equals(reversedCard)) {
                return true;
            }

            chances--;
        }

        BlackListManager.addCard(card);
        throw new WrongPinException("Hibás PIN! A kártyát letiltottuk!");
    }

    private static void dispenseCash(int amount) throws WrongAmountException, InvalidInputException {
        int remaining = amount;

        ArrayList<Integer> used = new ArrayList<>();

        for (Banknotes note : notes) {
            int denom = note.getDenomination();
            int available = note.getCount();

            int needed = remaining / denom;
            int usedCount = Math.min(needed, available);

            remaining -= usedCount * denom;
            used.add(usedCount);
        }

        if (remaining != 0) {
            throw new WrongAmountException(
                    "Az összeg nem adható ki a rendelkezésre álló bankjegyekkel!"
            );
        }

        for (int i = 0; i < notes.size(); i++) {
            try {
                notes.get(i).changeCount(-used.get(i));
            } catch (WrongAmountException e) {
                throw new WrongAmountException("Hiba a bankjegykezelés során!");
            }
        }

        saveNotesToFile();

        System.out.println("Kiadott bankjegyek:");
        for (int i = 0; i < notes.size(); i++) {
            if (used.get(i) > 0) {
                System.out.println(
                        notes.get(i).getDenomination() + " Ft: " + used.get(i) + " db"
                );
            }
        }
    }


    private static void saveNotesToFile() throws InvalidInputException {
        try (java.io.PrintWriter writer =
                     new java.io.PrintWriter(new java.io.FileWriter("NoteSave.txt"))) {

            for (Banknotes note : notes) {
                writer.println(note.getDenomination() + ";" + note.getCount());
            }

        } catch (IOException e) {
            throw new InvalidInputException("Nem sikerült menteni a bankjegyeket fájlba!");
        }
    }


}

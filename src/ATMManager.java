import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ATMManager {

    private static Scanner scanner;
    private static ArrayList<Banknotes> notes = new ArrayList<>();
    private static boolean retry = false;

    public static void init() {
        scanner = new Scanner(System.in);
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
        String input = scanner.nextLine().trim();
        int command;
        try {
            command = Integer.parseInt(input);
            switch (command) {
                case 1:
                    withdraw();
                    welcome();
                    break;
                case 2:
                    Admin.adminMode();
                    welcome();
                    break;
                case 3:
                    System.out.println("Viszont látásra!");
                    scanner.close();
                    System.exit(0);
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
        try {
            Card card = checkCardNumber(scanner);
            if (checkPin(card, scanner) || retry) {
                retry = false;
                System.out.println("Mennyi pénzt szeretne felvenni? (1000-2000000 Ft)");

                String amountInput = scanner.nextLine().trim();

                if (amountInput.isEmpty()) {
                    System.out.println("Számot adj meg!");
                    retry = true;
                    withdraw();
                    return;
                }

                if (!amountInput.matches("[0-9]+")) {
                    System.out.println("Számot adj meg!");
                    retry = true;
                    withdraw();
                    return;
                }

                try {
                    long amountLong = Long.parseLong(amountInput);

                    if (amountLong < 1000) {
                        throw new WrongAmountException("Minimum 1000 Ft-ot kell felvenni!");
                    }

                    if (amountLong > 2000000) {
                        throw new WrongAmountException("MAX 2 Millió vehető fel egyszerre!");
                    }

                    dispenseCash((int) amountLong);
                } catch (NumberFormatException nfe) {
                    System.out.println("MAX 2 Millió vehető fel egyszerre!");
                    retry = true;
                    withdraw();
                }
            }
        } catch (InvalidInputException iie) {
            System.out.println(iie.getMessage());
            welcome();
        } catch (WrongPinException wpe) {
            System.out.println(wpe.getMessage());
            welcome();
        } catch (WrongAmountException wae) {
            System.out.println(wae.getMessage());
            welcome();
        } catch (NumberFormatException nfe) {
            System.out.println("Csak számot adjon meg!");
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

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (!token.matches("[0-9]{4}")) {
                throw new InvalidInputException("A kártyaszám csak számokat tartalmazhat!");
            }
        }

        Card card = new Card(cardNum);
        if (BlackListManager.isBlacklisted(card)) {
            throw new InvalidInputException("Ez a kártya tiltva van!");
        }

        return card;
    }

    private static boolean checkPin(Card card, Scanner scanner) throws WrongPinException {
        int chances = 3;
        String reversedCard =
                new StringBuilder(card.getCardNumber().substring(0, 4))
                        .reverse()
                        .toString();
        while (chances > 0) {
            if (chances < 3) {
                System.out.println("Hibás PIN kód!");
            }

            System.out.print("Írja be a PIN kódot (4 számjegy): ");
            String pin = scanner.nextLine().trim();

            if (!pin.matches("^[0-9]{4}$")) {
                throw new WrongPinException("A PIN pontosan 4 számjegyből kell, hogy álljon!");
            }

            if (pin.equals(reversedCard)) {
                return true;
            }

            chances--;
        }

        BlackListManager.addCard(card);
        throw new WrongPinException("Hibás PIN! A kártyát letiltottuk!");
    }

    private static void dispenseCash(int amount)
            throws WrongAmountException, InvalidInputException {
        if (amount <= 0) {
            throw new WrongAmountException("Az összegnek nagyobbnak kell lennie 0-nál!");
        }

        if (amount % 1000 != 0) {
            throw new WrongAmountException("Csak 1000 Ft-os többszöröse adható ki!");
        }

        int remaining = amount;
        ArrayList<Integer> used = new ArrayList<>();
        for (int i = 0; i < notes.size(); i++) {
            used.add(0);
        }

        for (int i = 0; i < notes.size(); i++) {
            Banknotes note = notes.get(i);
            int denom = note.getDenomination();
            int available = note.getCount();
            if (remaining >= denom && available > 0) {
                int needed = remaining / denom;
                int usedCount = Math.min(needed, available);
                used.set(i, usedCount);
                remaining -= usedCount * denom;
            }
        }

        if (remaining != 0) {
            throw new WrongAmountException(
                    "Az összeg (" + amount + " Ft) nem adható ki a rendelkezésre álló bankjegyekkel!\n" +
                            "Maradék: " + remaining + " Ft"
            );
        }

        for (int i = 0; i < notes.size(); i++) {
            int newCount = notes.get(i).getCount() - used.get(i);
            if (newCount < 0) {
                throw new WrongAmountException("Nincs elég " + notes.get(i).getDenomination() + " Ft-os bankjegy!");
            }
        }

        for (int i = 0; i < notes.size(); i++) {
            notes.get(i).changeCount(-used.get(i));
        }

        saveNotesToFile();
        System.out.println("\n✓ Sikeres kifizetés: " + amount + " Ft");
        System.out.println("Kiadott bankjegyek:");
        boolean first = true;
        for (int i = 0; i < notes.size(); i++) {
            if (used.get(i) > 0) {
                if (!first) {
                    System.out.print(", ");
                }
                System.out.print(notes.get(i).getDenomination() + " Ft: " + used.get(i) + " db");
                first = false;
            }
        }
        System.out.println("\n");
        welcome();
    }

    public static void saveNotesToFile() throws InvalidInputException {
        try (java.io.PrintWriter writer =
                     new java.io.PrintWriter(new java.io.FileWriter("NoteSave.txt"))) {
            for (Banknotes note : notes) {
                writer.println(note.getDenomination() + ";" + note.getCount());
            }
        } catch (IOException e) {
            throw new InvalidInputException("Nem sikerült menteni a bankjegyeket fájlba!");
        }
    }

    public static ArrayList<Banknotes> getNotes() {
        return notes;
    }

    public static Scanner getScanner() {
        return scanner;
    }
}

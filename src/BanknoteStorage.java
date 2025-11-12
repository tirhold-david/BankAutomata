public class BanknoteStorage {
    private int[] denominations; //címletek
    private int[] counts;        //darabszám

    public BanknoteStorage() {
        this.denominations = new int[]{20000, 10000, 5000, 2000, 1000};
        this.counts = new int[]{10, 10, 10, 10, 10};
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
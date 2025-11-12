import java.util.ArrayList;
import java.util.List;

public class BanknoteStorage {
    private List<Banknotes> banknotes;

    public BanknoteStorage() {
        banknotes = new ArrayList<>();
        banknotes.add(new Banknotes(20000, 10));
        banknotes.add(new Banknotes(10000, 10));
        banknotes.add(new Banknotes(5000, 10));
        banknotes.add(new Banknotes(2000, 10));
        banknotes.add(new Banknotes(1000, 10));
    }

    public void printStorageStatus() {
        System.out.println("Bankjegy készlet:");
        for (Banknotes b : banknotes) {
            System.out.println(b);
        }
    }

    public int getCountForDenomination(int denomination) {
        for (Banknotes b : banknotes) {
            if (b.getDenomination() == denomination) {
                return b.getCount();
            }
        }
        return -1;
    }

    public boolean addBanknotes(int denomination, int amount) {
        for (Banknotes b : banknotes) {
            if (b.getDenomination() == denomination) {
                b.addCount(amount);
                return true;
            }
        }
        return false;
    }

    public List<Banknotes> getBanknotes() {
        return new ArrayList<>(banknotes);
    }
}

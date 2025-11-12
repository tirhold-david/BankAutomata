public class Banknotes {
    private int denomination; // címlet
    private int count;        // darabszám

    public Banknotes(int denomination, int count) {
        this.denomination = denomination;
        this.count = count;
    }

    public int getDenomination() {
        return denomination;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void addCount(int amount) {
        if (amount > 0) {
            this.count += amount;
        }
    }

    public void removeCount(int amount) {
        if (amount > 0 && amount <= count) {
            this.count -= amount;
        }
    }

    @Override
    public String toString() {
        return denomination + " Ft: " + count + " db";
    }
}

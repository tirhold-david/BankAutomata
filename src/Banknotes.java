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

    public void changeCount(int amount) throws WrongAmountException {
        if (amount > 0) {
            if (amount + getCount() <= 100) {
                setCount(getCount() + amount);
            } else {
                throw new WrongAmountException("Az össz érték maximum 100 lehet!");
            }
        } else if (amount < 0) {
            if (getCount() - amount >= 0) {
                setCount(getCount() - amount);
            } else {
                throw new WrongAmountException("Az össz érték minimum 0 lehet!");
            }
        } else {
            throw new WrongAmountException("Az érték nem lehet 0!");
        }
    }

    @Override
    public String toString() {
        return denomination + " Ft: " + count + " db";
    }
}

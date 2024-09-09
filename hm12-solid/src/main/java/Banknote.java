class Banknote {
    private final int denomination;

    public Banknote(int denomination) {
        this.denomination = denomination;
    }

    public int getDenomination() {
        return denomination;
    }

    @Override
    public String toString() {
        return denomination + "₽";
    }
}
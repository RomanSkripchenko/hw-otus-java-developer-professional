import java.util.Map;

class ATM {
    private final CashDispenser cashDispenser;

    public ATM(CashDispenser cashDispenser) {
        this.cashDispenser = cashDispenser;
    }

    public Map<Banknote, Integer> withdraw(int amount) {
        return cashDispenser.withdraw(amount);
    }

    public int checkBalance() {
        return cashDispenser.getTotalBalance();
    }
}
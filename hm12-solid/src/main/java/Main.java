import java.util.Map;

public class Main {
    public static void main(String[] args) {
        CashDispenser dispenser = new CashDispenser();
        ATM atm = new ATM(dispenser);

        Banknote fifty = new Banknote(50);
        Banknote hundred = new Banknote(100);
        Banknote fiveHundred = new Banknote(500);

        dispenser.addBanknotes(fifty, 10);
        dispenser.addBanknotes(hundred, 5);
        dispenser.addBanknotes(fiveHundred, 2);

        System.out.println("Total Balance: " + atm.checkBalance() + "₽");

        Map<Banknote, Integer> withdrawn = atm.withdraw(650);
        if (withdrawn.isEmpty()) {
            System.out.println("Error: Unable to withdraw the requested amount.");
        } else {
            System.out.println("Withdrawn:");
            for (Map.Entry<Banknote, Integer> entry : withdrawn.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue() + " pcs");
            }
        }

        System.out.println("Total Balance after withdrawal: " + atm.checkBalance() + "₽");
    }
}
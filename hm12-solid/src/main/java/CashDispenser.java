import java.util.*;

class CashDispenser {
    private final Map<Banknote, Integer> storage = new HashMap<>();

    public void addBanknotes(Banknote banknote, int quantity) {
        storage.put(banknote, storage.getOrDefault(banknote, 0) + quantity);
    }

    public int getTotalBalance() {
        return storage.entrySet().stream()
                .mapToInt(entry -> entry.getKey().getDenomination() * entry.getValue())
                .sum();
    }

    public Map<Banknote, Integer> withdraw(int amount) {
        Map<Banknote, Integer> result = new LinkedHashMap<>();
        List<Banknote> banknotes = new ArrayList<>(storage.keySet());
        Collections.sort(banknotes, Comparator.comparingInt(Banknote::getDenomination).reversed());

        for (Banknote banknote : banknotes) {
            int denomination = banknote.getDenomination();
            int quantity = storage.get(banknote);
            if (amount >= denomination && quantity > 0) {
                int needed = amount / denomination;
                int toWithdraw = Math.min(needed, quantity);
                if (toWithdraw > 0) {
                    result.put(banknote, toWithdraw);
                    amount -= toWithdraw * denomination;
                }
            }
        }

        if (amount > 0) {
            return Collections.emptyMap(); // Not enough money
        } else {
            updateStorage(result);
            return result;
        }
    }

    private void updateStorage(Map<Banknote, Integer> result) {
        for (Map.Entry<Banknote, Integer> entry : result.entrySet()) {
            Banknote banknote = entry.getKey();
            int quantity = entry.getValue();
            storage.put(banknote, storage.get(banknote) - quantity);
        }
    }
}
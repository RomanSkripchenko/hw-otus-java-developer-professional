package homework;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomerService {
    private final NavigableMap<Customer, String> customerMap = new TreeMap<>();

    public void add(Customer customer, String data) {
        customerMap.put(new Customer(customer.getId(), customer.getName(), customer.getScores()), data);
    }

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> entry = customerMap.firstEntry();
        if (entry != null) {
            return Map.entry(new Customer(entry.getKey().getId(), entry.getKey().getName(), entry.getKey().getScores()), entry.getValue());
        }
        return null;
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> entry = customerMap.higherEntry(customer);
        if (entry != null) {
            return Map.entry(new Customer(entry.getKey().getId(), entry.getKey().getName(), entry.getKey().getScores()), entry.getValue());
        }
        return null;
    }
}

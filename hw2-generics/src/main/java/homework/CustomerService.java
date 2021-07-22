package homework;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    private TreeMap<Customer, String> map = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        return cloneEntity(map.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Optional<Map.Entry<Customer, String>> entry = Optional.ofNullable(map.higherEntry(customer));
        return entry.map(this::cloneEntity).orElse(null);
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }

    private Map.Entry<Customer, String> cloneEntity(Map.Entry<Customer, String> entry) {
        Customer customer = entry.getKey();
        return new Entry<>(new Customer(customer.getId(), customer.getName(), customer.getScores()),
                            entry.getValue());
    }

    static final class Entry<Customer, String> implements Map.Entry<Customer, String> {
        private final Customer customer;
        private String value;

        public Entry(Customer customer, String value) {
            this.customer = customer;
            this.value = value;
        }

        @Override
        public Customer getKey() {
            return customer;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String setValue(String value) {
            this.value = value;
            return this.value;
        }
    }
}

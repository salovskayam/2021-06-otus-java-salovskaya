package homework;

import java.util.LinkedList;

public class CustomerReverseOrder {

    //todo: 2. надо реализовать методы этого класса
    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"
    private LinkedList<Customer> map = new LinkedList<>();

    public void add(Customer customer) {
        map.add(customer);
    }

    public Customer take() {
        return map.pollLast(); // это "заглушка, чтобы скомилировать"
    }
}

package ru.oop2;

import java.util.List;


public interface Atm {

    void putCash(List<Banknote> banknotes);

    List<Banknote> getCash(long amountOfMoney);

    long checkBalance();

}

package ru.oop2;

import java.util.List;

public interface CashStorage {

    List<Banknote> getAllBanknotes();

    void putBanknotes(List<Banknote> banknotes);

    void removeBanknotes(List<Banknote> banknotes);

}

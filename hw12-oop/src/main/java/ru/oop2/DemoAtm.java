package ru.oop2;

import java.util.*;

public class DemoAtm {

    public static void main(String[] args) {
        Banknote banknote_1000 = new Banknote(1000);
        Banknote banknote_500 = new Banknote(500);
        Banknote banknote_50 = new Banknote(50);
        Banknote banknote_10 = new Banknote(10);

        AtmImpl atm = new AtmImpl(new CashStorageImpl(), new CashIssueByOrderDescServiceImpl());

        atm.putCash(Arrays.asList(banknote_10, banknote_1000, banknote_50, banknote_500));

        System.out.println(atm.checkBalance());

        atm.getCash(1010);

        System.out.println(atm.checkBalance());

        atm.getCash(550);

        System.out.println(atm.checkBalance());

        atm.putCash(Arrays.asList(banknote_1000));

        System.out.println(atm.checkBalance());

        try {
            atm.getCash(2000);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}

package ru.oop2;


import java.util.List;

public interface CashIssueService {

    List<Banknote> getBanknotes(List<Banknote> srcBanknotes, long amountOfMoney);

}

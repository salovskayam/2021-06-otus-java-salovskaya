package ru.oop2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CashIssueByOrderDescServiceImpl implements CashIssueService {

    /**
     * @param srcBanknotes неотсортированный список с дубликатами всех имеющихся в атм банкнот
     * @param amountOfMoney запрошенная сумма
     * @throws StorageHasNoMoneyException если в атм нет денег или сумму нельзя выдать существующими банкнотами
     * @return отсортированный в порядке убывания номинала список с дубликатами банкнот для выдачи
     */
    @Override
    public List<Banknote> getBanknotes(List<Banknote> srcBanknotes, long amountOfMoney) {

        if (srcBanknotes.isEmpty()) {
            throw new StorageHasNoMoneyException("Storage has no money");
        }

        List<Banknote> orderedDescBanknotes = new ArrayList<>(srcBanknotes);
        orderedDescBanknotes.sort(Collections.reverseOrder(Comparator.comparingInt(Banknote::getValue)));

        long remainder = 0;
        List<Banknote> resultBanknotes = new ArrayList<>();

         for (Banknote banknote : orderedDescBanknotes) {
            remainder = amountOfMoney - banknote.getValue();

            if (remainder >= 0) {
               resultBanknotes.add(banknote);
               amountOfMoney = remainder;
            }

            if (remainder == 0)
                break;

        }

        if (remainder != 0) {
            throw new StorageHasNoMoneyException("Storage has no money for issue");
        }

        return Collections.unmodifiableList(resultBanknotes);

    }
}

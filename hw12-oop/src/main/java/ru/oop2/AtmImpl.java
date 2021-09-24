package ru.oop2;


import java.util.List;

public class AtmImpl implements Atm {

    private final CashStorage cashStorage;
    private final CashIssueService cashIssueService;

    public AtmImpl(CashStorage cashStorage, CashIssueService cashIssueService) {
        this.cashStorage = cashStorage;
        this.cashIssueService = cashIssueService;
    }

    @Override
    public void putCash(List<Banknote> banknotes) {
        cashStorage.putBanknotes(banknotes);
    }

    @Override
    public List<Banknote> getCash(long amountOfMoney) {
        List<Banknote> banknotes = cashIssueService.getBanknotes(cashStorage.getAllBanknotes(), amountOfMoney);
        cashStorage.removeBanknotes(banknotes);
        return banknotes;
    }

    @Override
    public long getBalance() {
        return cashStorage.getAllBanknotes().stream()
                .map(Banknote::getValue)
                .reduce(0, Integer::sum);
    }
}

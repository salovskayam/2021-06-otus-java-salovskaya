package ru.oop2;

import java.util.*;

public class CashStorageImpl implements CashStorage {

    private Map<Banknote, Integer> banknotes = new HashMap<>();

    @Override
    public List<Banknote> getAllBanknotes() {

        List<Banknote> allBanknotes = new ArrayList<>();

        for (Map.Entry<Banknote, Integer> entry : this.banknotes.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                allBanknotes.add(entry.getKey());
            }
        }
        return allBanknotes;

    }

    /**
     *
     * @param banknotes неотсортированный список вносимых банкнот с дубликатами
     */
    @Override
    public void putBanknotes(List<Banknote> banknotes) {

        banknotes.forEach(banknote -> this.banknotes.compute(
                banknote, (key, value) -> (value == null) ? 1 : ++value));

    }

    /**
     * Уменьшаем счетчики для всех запрошенных банкнот разом, либо выбрасываем ошибку
     *
     * @param banknotes список изымающихся банкнот с дубликатами
     * @throws StorageHasNoMoneyException выбрасываем ошибку, если банкноты данного номинала
     * еще не было в хранилище, либо счетчик банкноты уже равен 0
     */
    @Override
    public void removeBanknotes(List<Banknote> banknotes) {
        Map<Banknote, Integer> destBanknotes = new HashMap<>(this.banknotes);

        banknotes.forEach(banknote -> destBanknotes.compute(
                banknote, (key, value) -> {
                    if (value == null || value <= 0) {
                        throw new StorageHasNoMoneyException("Storage has no money for issue");
                    }

                    return --value;
                }));

        this.banknotes = new HashMap<>(destBanknotes);
    }
}

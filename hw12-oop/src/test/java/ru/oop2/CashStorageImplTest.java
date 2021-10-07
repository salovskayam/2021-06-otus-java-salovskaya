package ru.oop2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Класс CashStorageImplTest")
class CashStorageImplTest {

    private Banknote banknote_1000;
    private Banknote banknote_500;
    private Banknote banknote_100;
    private Banknote banknote_50;
    private Banknote banknote_10;
    private CashStorage cashStorage;

    @BeforeEach
    void setUp() {
        banknote_1000 = new Banknote(1000);
        banknote_500 = new Banknote(500);
        banknote_100 = new Banknote(100);
        banknote_50 = new Banknote(50);
        banknote_10 = new Banknote(10);

        cashStorage = new CashStorageImpl();
    }

    @Test
    @DisplayName("сохранение нескольких банкнот одного номинала")
    void putBanknotes() {
        cashStorage.putBanknotes(Arrays.asList(banknote_10, banknote_10));
        cashStorage.putBanknotes(Arrays.asList(banknote_10, banknote_10, banknote_10));

        assertThat(cashStorage.getAllBanknotes()).hasSize(5);
    }

    @Test
    @DisplayName("если еще не сохраняли банкноты, то возвращается пустой лист")
    void getAllBanknotes1() {
        assertThat(cashStorage.getAllBanknotes().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("возвращаемый список банкнот содержит все банкноты и в случае дубликатов")
    void getAllBanknotes2() {
        cashStorage.putBanknotes(Arrays.asList(banknote_10, banknote_1000, banknote_100, banknote_10));

        assertThat(cashStorage.getAllBanknotes()).containsExactlyInAnyOrder(
                banknote_10,
                banknote_10,
                banknote_100,
                banknote_1000);
    }

    @Test
    @DisplayName("корректное уменьшение количества запрашиваемых банкнот из множества других и дубликатов")
    void removeBanknotes1() {
        cashStorage.putBanknotes(Arrays.asList(banknote_10, banknote_1000, banknote_100, banknote_10));
        cashStorage.removeBanknotes(Arrays.asList(banknote_1000, banknote_10));

        assertThat(cashStorage.getAllBanknotes()).containsExactlyInAnyOrder(banknote_10, banknote_100);
    }

    @Test
    @DisplayName("если сохранили банкноты, а потом все их удалили, то возвращается пустой лист")
    void removeBanknotes2() {
        cashStorage.putBanknotes(Arrays.asList(banknote_10, banknote_10));
        cashStorage.removeBanknotes(Arrays.asList(banknote_10, banknote_10));

        assertThat(cashStorage.getAllBanknotes().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("ошибка, если удаляем банкноты из пустого хранилища")
    void removeBanknotes3() {
        assertThrows(StorageHasNoMoneyException.class, () ->
                cashStorage.removeBanknotes(Arrays.asList(banknote_10)));
    }

    @Test
    @DisplayName("ошибка, если удаляем банкноту, которая уже удалена из хранилища")
    void removeBanknotes4() {
        cashStorage.putBanknotes(Arrays.asList(banknote_10, banknote_100));
        cashStorage.removeBanknotes(Arrays.asList(banknote_10));

        assertThrows(StorageHasNoMoneyException.class, () ->
                cashStorage.removeBanknotes(Arrays.asList(banknote_10)));

        assertThat(cashStorage.getAllBanknotes()).containsExactlyInAnyOrder(banknote_100);
    }

    @Test
    @DisplayName("удаляем либо все запрошенные банкноты разом, либо выбрасываем ошибку")
    void removeBanknotes5() {
        cashStorage.putBanknotes(Arrays.asList(banknote_10));

        assertThrows(StorageHasNoMoneyException.class, () ->
                cashStorage.removeBanknotes(Arrays.asList(banknote_10, banknote_10)));

        assertThat(cashStorage.getAllBanknotes()).containsExactlyInAnyOrder(banknote_10);
    }

}
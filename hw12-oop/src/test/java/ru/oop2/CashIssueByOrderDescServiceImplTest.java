package ru.oop2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("Класс CashIssueByOrderDescServiceImpl")
class CashIssueByOrderDescServiceImplTest {

    private Banknote banknote_1000;
    private Banknote banknote_500;
    private Banknote banknote_100;
    private Banknote banknote_50;
    private CashIssueService cashIssueService;

    @BeforeEach
    void setUp() {
        banknote_1000 = new Banknote(1000);
        banknote_500 = new Banknote(500);
        banknote_100 = new Banknote(100);
        banknote_50 = new Banknote(50);
        cashIssueService = new CashIssueByOrderDescServiceImpl();
    }

    @Test
    @DisplayName("выдача самой большой банкноты")
    void getBanknotesTest1() {
        List<Banknote> srcBanknotes = Arrays.asList(banknote_1000, banknote_100);

        assertThat(cashIssueService.getBanknotes(srcBanknotes, 1000))
                .containsExactly(banknote_1000);
    }

    @Test
    @DisplayName("выдача самой маленькой банкноты")
    void getBanknotesTest2() {
        List<Banknote> srcBanknotes = Arrays.asList(banknote_1000, banknote_100);

        assertThat(cashIssueService.getBanknotes(srcBanknotes, 100))
                .containsExactly(banknote_100);
    }

    @Test
    @DisplayName("выдача запрашиваемой суммы банкнотами в порядке убывания")
    void getBanknotesTest3() {
        List<Banknote> srcBanknotes = Arrays.asList(
                banknote_50, banknote_1000, new Banknote(2000),
                banknote_100, banknote_500, banknote_100, new Banknote(10));

        assertThat(cashIssueService.getBanknotes(srcBanknotes, 1150))
                .containsExactly(banknote_1000, banknote_100, banknote_50);
    }

    @Test
    @DisplayName("выдача запрашиваемой суммы всеми имеющимися банкнотами в порядке убывания")
    void getBanknotesTest4() {
        List<Banknote> srcBanknotes = Arrays.asList(banknote_50, banknote_500);

        assertThat(cashIssueService.getBanknotes(srcBanknotes, 550))
                .containsExactly(banknote_500, banknote_50);
    }

    @Test
    @DisplayName("выдача запрашиваемой суммы одной единственной банкнотой")
    void getBanknotesTest6() {
        List<Banknote> srcBanknotes = Arrays.asList(banknote_50);

        assertThat(cashIssueService.getBanknotes(srcBanknotes, 50))
                .containsExactly(banknote_50);
    }

    @Test
    @DisplayName("вывести ошибку, когда в банкомате вообще нет денег")
    void getBanknotesTest7() {
        Exception ex = assertThrows(StorageHasNoMoneyException.class, () ->
                cashIssueService.getBanknotes(new ArrayList<>(),
                        1000));
        assertTrue(ex.getMessage().contains("Storage has no money"));
    }

    @Test
    @DisplayName("вывести ошибку, если нельзя выдать запрошенную сумму существующими банкнотами")
    void getBanknotesTest8() {
        Exception ex = assertThrows(StorageHasNoMoneyException.class, () ->
                cashIssueService.getBanknotes(Arrays.asList(banknote_1000, banknote_500),
                        1100));
        assertTrue(ex.getMessage().contains("Storage has no money for issue"));
    }

    @Test
    @DisplayName("вывести ошибку, если в банкомате не хватает денег для выдачи")
    void getBanknotesTest9() {
        Exception ex = assertThrows(StorageHasNoMoneyException.class, () ->
                cashIssueService.getBanknotes(Arrays.asList(banknote_500, banknote_50),
                        1000));
        assertTrue(ex.getMessage().contains("Storage has no money for issue"));
    }
}
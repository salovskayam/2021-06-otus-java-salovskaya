package ru.oop2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AtmImplTest {

    private Banknote banknote_1000;
    private CashStorage cashStorage;
    private CashIssueService cashIssueService;
    private Atm atm;

    @BeforeEach
    void setUp() {
        banknote_1000 = new Banknote(1000);
        cashStorage = new CashStorageImpl();
        cashIssueService = new CashIssueByOrderDescServiceImpl();
        atm = new AtmImpl(cashStorage, cashIssueService);
    }

    @Test
    void checkBalance1() {
         assertEquals(0, atm.getBalance());
    }

    @Test
    void checkBalance2() {
        atm.putCash(Arrays.asList(banknote_1000));
        assertEquals(1000, atm.getBalance());
    }

    @Test
    void checkBalance3() {
        atm.putCash(Arrays.asList(banknote_1000, banknote_1000));
        atm.getCash(1000);
        assertEquals(1000, atm.getBalance());
    }

    @Test
    void checkBalance4() {
        atm.putCash(Arrays.asList(banknote_1000, banknote_1000));
        atm.getCash(atm.getBalance());
        assertEquals(0, atm.getBalance());
    }

    @Test
    void checkBalance6() {
        atm.putCash(Arrays.asList(banknote_1000));
        atm.getCash(1000);
        atm.putCash(Arrays.asList(banknote_1000));
        assertEquals(1000, atm.getBalance());
    }

    @Test
    void checkBalance7() {
        atm.putCash(Arrays.asList(banknote_1000));
        assertThrows(StorageHasNoMoneyException.class, () -> atm.getCash(2000));
    }

}
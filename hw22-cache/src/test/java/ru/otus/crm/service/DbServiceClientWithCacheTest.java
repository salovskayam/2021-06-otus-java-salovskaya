package ru.otus.crm.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.base.AbstractHibernateTest;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;

import static org.assertj.core.api.Assertions.assertThat;

class DbServiceClientWithCacheTest extends AbstractHibernateTest {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientWithCacheTest.class);

    @Test
    @DisplayName("Кэш + СУБД работает быстрее СУБД")
    void shouldCorrectSaveClientInCache() {
        //given
        var client = new Client("Petya");
        client.setAddress(new Address("street1"));
        client.addPhone(new Phone("990-89-89"));
        client.addPhone(new Phone("1000-89-89"));

        //when
        for (int i = 1; i < 1001; i++) {
            dbServiceClient.saveClient(client);
        }

        //then
        long startTime = System.currentTimeMillis();
        for (int i = 1; i < 128; i++) {
            dbServiceClient.getClient(i);
        }
        long deltaCacheAndDb = System.currentTimeMillis() - startTime;

        //when
        //очищаем кэш, чтобы получить значение из БД
        dbServiceClient.getCache().clear();

        //then
        startTime = System.currentTimeMillis();
        for (int i = 1; i < 128; i++) {
            dbServiceClient.getClient(i);
        }
        long deltaDB = System.currentTimeMillis() - startTime;
        log.info("clientFromCacheAndDb за время: {} мс", deltaCacheAndDb);
        log.info("clientFromDb за время: {} мс", deltaDB);

        assertThat(deltaCacheAndDb < deltaDB).isTrue();
    }

    @Test
    @DisplayName("Кэш сбрасывается при недостатке памяти")
    void shouldCorrectSaveClientInDBandCache() throws InterruptedException {
        //given
        var client = new Client("Petya");
        client.setAddress(new Address("street1"));
        client.addPhone(new Phone("990-89-89"));
        client.addPhone(new Phone("1000-89-89"));

        //when
        for (int i = 0; i < 150; i++) {
            dbServiceClient.saveClient(client);
        }
        var sizeBeforeGc = dbServiceClient.getCache().size();
        log.info("Cache size before gc: {}", sizeBeforeGc);
        System.gc();
        Thread.sleep(1000);

        //then
        var sizeAfterGc = dbServiceClient.getCache().size();
        log.info("Cache size after gc: {}", sizeAfterGc);

        assertThat(sizeAfterGc).isEqualTo(127);
    }
}
package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientImpl;

// docker exec -it postgres:12 bash
// psql -U usr demoDB
public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
///
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        var clientFirst = new Client("dbServiceFirst");

        var address1 = new Address("street1");
        clientFirst.setAddress(address1);

        var phone1 = new Phone("123-456");
        var phone2 = new Phone("312-888");
        clientFirst.addPhone(phone1);
        clientFirst.addPhone(phone2);

        dbServiceClient.saveClient(clientFirst);

        var clientSecond = new Client("dbServiceSecond");
        var address2 = new Address("street2");
        clientSecond.setAddress(address2);

        var phone3 = new Phone("999-456");
        clientSecond.addPhone(phone3);

        var savedClientSecond = dbServiceClient.saveClient(clientSecond);

        var clientSecondSelected = dbServiceClient.getClient(savedClientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + savedClientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);
///

        Client newClient = new Client(clientSecondSelected.getId(), "dbServiceSecondUpdated");
        newClient.setAddress(clientSecondSelected.getAddress());
        clientSecondSelected.getPhones().forEach(newClient::addPhone);

        dbServiceClient.saveClient(newClient);
        var clientUpdated = dbServiceClient.getClient(clientSecondSelected.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondSelected.getId()));
        log.info("clientUpdated:{}", clientUpdated);

        log.info("All clients");
        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));
    }
}

package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.*;
import ru.otus.crm.service.DbServiceClientImpl;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        try {
            var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
            var dbUrl = configuration.getProperty("hibernate.connection.url");
            var dbUserName = configuration.getProperty("hibernate.connection.username");
            var dbPassword = configuration.getProperty("hibernate.connection.password");

            new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

            //var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class);

            var sessionFactory = HibernateUtils.buildSessionFactory(
                    configuration,
                    Client.class,
                    Address.class,
                    Phone.class // добавляем связанные сущности
            );

            var transactionManager = new TransactionManagerHibernate(sessionFactory);
            var clientTemplate = new DataTemplateHibernate<>(Client.class);
            var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

            dbServiceClient.saveClient(new Client("dbServiceFirst"));

            System.out.println("Initialization completed successfully.");
        } catch (Exception e) {
            e.printStackTrace(); // Вывод полного стека ошибки в консоль
        }
    }
}

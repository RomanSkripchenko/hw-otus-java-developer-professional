package ru.otus.crm.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import otus.cachehw.HwCache;
import otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Client;
public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;
    private final HwCache<Long, Client> clientCache = new MyCache<>();

    public DbServiceClientImpl(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                client.setId(clientId);
            } else {
                dataTemplate.update(connection, client);
            }
            clientCache.put(client.getId(), client);
            log.info("saved client: {}", client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client cachedClient = clientCache.get(id);
        if (cachedClient != null) {
            log.info("Retrieved from cache: {}", cachedClient);
            return Optional.of(cachedClient);
        }

        return transactionRunner.doInTransaction(connection -> {
            Optional<Client> clientOptional = dataTemplate.findById(connection, id);
            clientOptional.ifPresent(client -> clientCache.put(id, client));
            log.info("Retrieved from DB: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            List<Client> clientList = dataTemplate.findAll(connection);
            clientList.forEach(client -> clientCache.put(client.getId(), client));
            log.info("Loaded all clients from DB");
            return clientList;
        });
    }
}


package ru.otus.crm.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.crm.repository.ClientRepository;
import ru.otus.crm.model.Client;
import ru.otus.sessionmanager.TransactionManager;


import java.util.List;
import java.util.Optional;

@Service
public class DBClientServiceImpl implements DBClientService {

    private static final Logger log = LoggerFactory.getLogger(DBClientServiceImpl.class);

    private final ClientRepository clientRepository;
    private final TransactionManager transactionManager;

    public DBClientServiceImpl(ClientRepository clientRepository, TransactionManager transactionManager) {
        this.clientRepository = clientRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public List<Client> findAll() {
        var clientList = clientRepository.findAll();
        log.info("clientList:{}", clientList);
        return clientList;
    }

    @Override
    public Optional<Client> findById(long id) {
        var optionalClient = clientRepository.findById(id);
        log.info("client: {}", optionalClient);
        return optionalClient;
    }

    @Override
    public Client save(Client client) {
        return transactionManager.doInTransaction(() -> {
            var savedClient = clientRepository.save(client);
            log.info("saved client: {}", savedClient);
            return savedClient;
        });
    }
}

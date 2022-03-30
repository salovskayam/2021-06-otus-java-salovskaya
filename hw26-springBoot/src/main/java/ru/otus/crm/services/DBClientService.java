package ru.otus.crm.services;

import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public interface DBClientService {
    List<Client> findAll();
    Optional<Client> findById(long id);
    Client save(Client client);
}

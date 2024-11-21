package app.service;

import app.model.Client;
import app.repository.ClientRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> findAll() {
        return (List<Client>) clientRepository.findAll();
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }
}

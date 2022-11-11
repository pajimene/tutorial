package com.ccsw.tutorial.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.tutorial.client.exceptions.IncorrectNameException;
import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Override
    public Client get(Long id) {
        return this.clientRepository.findById(id).orElse(null);
        // .orElseThrow(() -> new
        // ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Override
    public Client findByName(String name) {
        return this.clientRepository.findByName(name);
    }

    @Override
    public List<Client> findAll() {

        return (List<Client>) this.clientRepository.findAll();
    }

    @Override
    public void save(Long id, ClientDto dto) throws IncorrectNameException {
        Client client = null;

        if (id == null)
            client = new Client();
        else
            client = this.get(id);

        if (this.clientRepository.findByName(dto.getName()) == null) {
            client.setName(dto.getName());
            this.clientRepository.save(client);
        } else
            throw new IncorrectNameException("Este nombre ya existe");
    }

    @Override
    public void delete(Long id) {
        this.clientRepository.deleteById(id);
    }

}

package com.ccsw.tutorial.client;

import java.util.List;

import com.ccsw.tutorial.client.exceptions.IncorrectNameException;
import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;

public interface ClientService {

    /**
     * Recupera un {@link com.ccsw.tutorial.client.model Client} a partir de su ID
     * 
     * @param id
     * @return
     */
    Client get(Long id);

    /**
     * Recupera un {@link com.ccsw.tutorial.client.model.Client} a partir de su
     * nombre
     * 
     * @param id
     * @return
     */
    Client findByName(String name);

    /**
     * Método para búscar todos los clientes
     * 
     * @param dto
     * @return
     */
    List<Client> findAll();

    /**
     * Método para guardar un cliente
     * 
     * @param dto
     * @return
     */
    void save(Long id, ClientDto dto) throws IncorrectNameException;

    /**
     * Método para crear o actualizar una Category
     * 
     * @param dto
     * @return
     */
    void delete(Long id);
}

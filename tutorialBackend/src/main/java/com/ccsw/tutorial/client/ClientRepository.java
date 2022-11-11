package com.ccsw.tutorial.client;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ccsw.tutorial.client.model.Client;

/**
 * @author ccsw
 */
public interface ClientRepository extends CrudRepository<Client, Long> {
    /**
     * Método para recuperar un {@link com.ccsw.tutorial.client.model.Client} dado
     * su id
     * 
     * @param id
     * @return
     */
    @Query("select c from Client c where c.name = :name")
    Client findByName(@Param("name") String name);
}

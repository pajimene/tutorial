package com.ccsw.tutorial.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccsw.tutorial.client.exceptions.IncorrectNameException;
import com.ccsw.tutorial.client.model.ClientDto;
import com.devonfw.module.beanmapping.common.api.BeanMapper;

/**
 * @author ccsw
 */
@CrossOrigin(origins = "*")
@RequestMapping(value = "/client")
@RestController
public class ClientController {

    @Autowired
    ClientService clientService;

    @Autowired
    BeanMapper beanMapper;

    /**
     * Método para recuperar un {@link com.ccsw.tutorial.client.model.Client} dado
     * su id
     * 
     * @param id
     * @return
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)

    public ClientDto find(@PathVariable(name = "id") Long id) {
        return this.beanMapper.map(this.clientService.get(id), ClientDto.class);
    }

    /**
     * Método para recuperar todos los {@link com.ccsw.tutorial.client.model.Client}
     * 
     * @return
     */
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<ClientDto> findAll() {
        return this.beanMapper.mapList(this.clientService.findAll(), ClientDto.class);
    }

    /**
     * Método para crear o actualizar un
     * {@link com.ccsw.tutorial.client.model.Client}
     * 
     * @param dto
     * @return
     * @throws IncorrectNameException
     */
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody ClientDto dto)
            throws IncorrectNameException {
        try {
            this.clientService.save(id, dto);
        } catch (IncorrectNameException err) {
            throw new IncorrectNameException("Erorr cambiando el nombre ->" + err);
        }
    }

    /**
     * Método para borrar un {@link com.ccsw.tutorial.client.model.Client}
     * 
     * @param id
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {

        this.clientService.delete(id);

    }
}

package com.ccsw.tutorial.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ccsw.tutorial.client.exceptions.IncorrectNameException;
import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;

@ExtendWith(MockitoExtension.class)
public class ClientTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    public void findAllShouldReturnAllClients() {

        List<Client> list = new ArrayList<>();
        list.add(mock(Client.class));

        when(clientRepository.findAll()).thenReturn(list);

        List<Client> clients = clientService.findAll();

        assertNotNull(clients);
        assertEquals(1, clients.size());
    }

    public static final String NOT_EXISTS_CLIENT_NAME = "Elend";

    @Test
    public void saveNotExistsClientIdShouldInsert() {

        ClientDto clientDto = new ClientDto();
        clientDto.setName(NOT_EXISTS_CLIENT_NAME);

        ArgumentCaptor<Client> client = ArgumentCaptor.forClass(Client.class);

        try {
            clientService.save(null, clientDto);
        } catch (IncorrectNameException exception) {
            ;
        }

        verify(clientRepository).save(client.capture());

        assertEquals(NOT_EXISTS_CLIENT_NAME, client.getValue().getName());
    }

    public static final Long EXISTS_CLIENT_ID = 1L;

    @Test
    public void saveExistsClientIdShouldUpdate() {

        ClientDto clientDto = new ClientDto();
        clientDto.setName(NOT_EXISTS_CLIENT_NAME);

        Client client = mock(Client.class);
        when(clientRepository.findById(EXISTS_CLIENT_ID)).thenReturn(Optional.of(client));

        try {
            clientService.save(EXISTS_CLIENT_ID, clientDto);
        } catch (IncorrectNameException exception) {
            ;
        }

        verify(clientRepository).save(client);
    }

    public static final String EXISTS_CLIENT_NAME = "Kaladin";

    @Test
    public void searchByExistsNameShouldReturnClient() {

        Client client = mock(Client.class);
        when(client.getId()).thenReturn(EXISTS_CLIENT_ID);
        when(clientRepository.findByName(EXISTS_CLIENT_NAME)).thenReturn(client);

        Client clientResponse = clientService.findByName(EXISTS_CLIENT_NAME);

        assertNotNull(clientResponse);
        assertEquals(EXISTS_CLIENT_ID, client.getId());
    }

    @Test
    public void searchByNotExistsNameShouldReturnNull() {
        when(clientRepository.findByName(NOT_EXISTS_CLIENT_NAME)).thenReturn(null);

        Client clientResponse = clientService.findByName(NOT_EXISTS_CLIENT_NAME);

        assertNull(clientResponse);
    }

    @Test
    public void saveExistsNameShouldNameException() {
        ClientDto clientDto = new ClientDto();
        clientDto.setName(EXISTS_CLIENT_NAME);
        clientDto.setId(EXISTS_CLIENT_ID);

        Client client = mock(Client.class);
        when(clientRepository.findById(EXISTS_CLIENT_ID)).thenReturn(Optional.of(client));
        when(clientRepository.findByName(EXISTS_CLIENT_NAME)).thenReturn(client);

        try {
            clientService.save(EXISTS_CLIENT_ID, clientDto);
            fail("Este metodo debería fallar insertando otro nombre igual");
        } catch (IncorrectNameException exception) {
            ;
        }
    }

    @Test
    public void deleteExistsClientIdShouldDelete() {

        clientService.delete(EXISTS_CLIENT_ID);

        verify(clientRepository).deleteById(EXISTS_CLIENT_ID);
    }

    @Test
    public void getExistsClientIdShouldReturnClient() {

        Client client = mock(Client.class);
        when(client.getId()).thenReturn(EXISTS_CLIENT_ID);
        when(clientRepository.findById(EXISTS_CLIENT_ID)).thenReturn(Optional.of(client));

        Client clientResponse = clientService.get(EXISTS_CLIENT_ID);

        assertNotNull(clientResponse);
        assertEquals(EXISTS_CLIENT_ID, client.getId());
    }

    public static final Long NOT_EXISTS_CLIENT_ID = -1L;

    @Test
    public void getNotExistsClientIdShouldReturnNull() {

        when(clientRepository.findById(NOT_EXISTS_CLIENT_ID)).thenReturn(Optional.empty());

        Client client = clientService.get(NOT_EXISTS_CLIENT_ID);

        assertNull(client);
    }

}

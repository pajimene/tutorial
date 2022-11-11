package com.ccsw.tutorial.loan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.ccsw.tutorial.category.model.CategoryDto;
import com.ccsw.tutorial.client.ClientServiceImpl;
import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.game.GameServiceImpl;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;

@ExtendWith(MockitoExtension.class)
public class LoanTest {
    @Mock
    private LoanRepository loanRepository;

    @Mock
    private GameServiceImpl gameService;

    @Mock
    private ClientServiceImpl clientService;

    @InjectMocks
    private LoanServiceImpl loanService;

    @Test
    public void findWithoutFilterShouldFindAll() {
        List<Loan> list = new ArrayList<Loan>();
        list.add(mock(Loan.class));

        Page<Loan> page = new PageImpl<Loan>(list);

        LoanSearchDto dto = new LoanSearchDto();
        dto.setPageable(PageRequest.of(0, 1));

        when(loanRepository.findAll(dto.getPageable())).thenReturn(page);

        Page<Loan> loans = loanService.find(null, null, null, dto);

        assertNotNull(loans);
        assertEquals(1, loans.getContent().size());
    }

    public static final Long EXISTS_CLIENT_ID = 1L;

    @Test
    public void findByExistingNameShouldReturnLoan() {
        List<Loan> list = new ArrayList<Loan>();
        list.add(mock(Loan.class));

        Page<Loan> page = new PageImpl<Loan>(list);

        LoanSearchDto dto = new LoanSearchDto();
        dto.setPageable(PageRequest.of(0, 1));

        when(loanRepository.findBy(EXISTS_CLIENT_ID, null, null, dto.getPageable())).thenReturn(page);

        Page<Loan> loans = loanService.find(EXISTS_CLIENT_ID, null, null, dto);

        assertNotNull(loans);
        assertEquals(1, loans.getContent().size());
    }

    public static final Long EXISTS_GAME_ID = 1L;

    @Test
    public void findByExistingTitleShouldReturnLoan() {
        List<Loan> list = new ArrayList<Loan>();
        list.add(mock(Loan.class));

        Page<Loan> page = new PageImpl<Loan>(list);

        LoanSearchDto dto = new LoanSearchDto();
        dto.setPageable(PageRequest.of(0, 1));

        when(loanRepository.findBy(null, EXISTS_GAME_ID, null, dto.getPageable())).thenReturn(page);

        Page<Loan> loans = loanService.find(null, EXISTS_GAME_ID, null, dto);

        assertNotNull(loans);
        assertEquals(1, loans.getContent().size());
    }

    public Date EXIST_DATE = new Date(122, 11, 15);

    @Test
    public void findByExistingDateShouldReturnLoan() {
        List<Loan> list = new ArrayList<Loan>();
        list.add(mock(Loan.class));

        Page<Loan> page = new PageImpl<Loan>(list);

        LoanSearchDto dto = new LoanSearchDto();
        dto.setPageable(PageRequest.of(0, 1));

        when(loanRepository.findBy(null, null, EXIST_DATE, dto.getPageable())).thenReturn(page);

        Page<Loan> loans = loanService.find(null, null, EXIST_DATE, dto);

        assertNotNull(loans);
        assertEquals(1, loans.getContent().size());
    }

    public static final Long NOT_EXISTS_GAME_ID = 11L;

    @Test
    public void findByNotExistingShouldReturnNull() {

        LoanSearchDto dto = new LoanSearchDto();
        dto.setPageable(PageRequest.of(0, 1));

        when(loanRepository.findBy(null, NOT_EXISTS_GAME_ID, null, dto.getPageable())).thenReturn(null);

        Page<Loan> loans = loanService.find(null, NOT_EXISTS_GAME_ID, null, dto);

        assertNull(loans);
    }

    public static final Long NOT_CLIENT_GAME_ID = 11L;
    public static final String EXIST_CLIENT_NAME = "Kaladin";
    public static final String EXISTS_GAME_TITLE = "On Mars";
    public static final String EXISTS_CATEGORY_NAME = "Eurogames";
    public static final Long EXISTS_CATEGORY_ID = 1L;

    @Test
    public void saveNotExistingLoanShouldInsert() {

        ClientDto clientDto = new ClientDto();
        clientDto.setId(EXISTS_CLIENT_ID);

        CategoryDto catDto = new CategoryDto();
        catDto.setName(EXISTS_CATEGORY_NAME);
        catDto.setId(EXISTS_CATEGORY_ID);

        GameDto gameDto = new GameDto();
        gameDto.setId(EXISTS_GAME_ID);
        gameDto.setCategory(catDto);
        gameDto.setTitle(EXISTS_GAME_TITLE);

        LoanDto loanDto = new LoanDto();
        loanDto.setClient(clientDto);
        loanDto.setGame(gameDto);
        loanDto.setInitialDate(new Date(122, 11, 15));
        loanDto.setFinalDate(new Date(122, 11, 17));

        Client client = mock(Client.class);
        Game game = mock(Game.class);
        List<Game> gameList = new ArrayList<Game>();
        gameList.add(game);
        List<Loan> emptyList = new ArrayList<Loan>();

        when(loanRepository.checkClientHasLoan(EXISTS_CLIENT_ID, loanDto.getInitialDate(), loanDto.getFinalDate()))
                .thenReturn(emptyList);
        when(loanRepository.checkGameInLoan(EXISTS_GAME_ID, loanDto.getInitialDate(), loanDto.getFinalDate()))
                .thenReturn(emptyList);

        when(clientService.get(EXISTS_CLIENT_ID)).thenReturn(client);
        when(gameService.find(EXISTS_GAME_TITLE, EXISTS_CATEGORY_ID)).thenReturn(gameList);

        ArgumentCaptor<Loan> loanCaptor = ArgumentCaptor.forClass(Loan.class);

        try {
            loanService.save(loanDto);
        } catch (Exception exception) {
            System.out.println(exception);
        }
        verify(loanRepository).save(loanCaptor.capture());

    }

    @Test
    public void saveWithFinalBeforeIntialShouldException() {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(EXISTS_CLIENT_ID);

        CategoryDto catDto = new CategoryDto();
        catDto.setName(EXISTS_CATEGORY_NAME);
        catDto.setId(EXISTS_CATEGORY_ID);

        GameDto gameDto = new GameDto();
        gameDto.setId(EXISTS_GAME_ID);
        gameDto.setCategory(catDto);
        gameDto.setTitle(EXISTS_GAME_TITLE);

        LoanDto loanDto = new LoanDto();
        loanDto.setClient(clientDto);
        loanDto.setGame(gameDto);
        loanDto.setInitialDate(new Date(122, 11, 15));
        loanDto.setFinalDate(new Date(122, 11, 10));

        try {
            loanService.save(loanDto);
            fail("Este metodo debería fallar insertando otro nombre igual");
        } catch (Exception exception) {
            ;
        }

    }

    @Test
    public void saveWithExtendedDateShouldException() {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(EXISTS_CLIENT_ID);

        CategoryDto catDto = new CategoryDto();
        catDto.setName(EXISTS_CATEGORY_NAME);
        catDto.setId(EXISTS_CATEGORY_ID);

        GameDto gameDto = new GameDto();
        gameDto.setId(EXISTS_GAME_ID);
        gameDto.setCategory(catDto);
        gameDto.setTitle(EXISTS_GAME_TITLE);

        LoanDto loanDto = new LoanDto();
        loanDto.setClient(clientDto);
        loanDto.setGame(gameDto);
        loanDto.setInitialDate(new Date(122, 11, 03));
        loanDto.setFinalDate(new Date(122, 11, 25));

        try {
            loanService.save(loanDto);
            fail("Este metodo debería fallar insertando otro nombre igual");
        } catch (Exception exception) {
            ;
        }
    }

    @Test
    public void saveHavingOtherLoanShouldException() {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(EXISTS_CLIENT_ID);

        CategoryDto catDto = new CategoryDto();
        catDto.setName(EXISTS_CATEGORY_NAME);
        catDto.setId(EXISTS_CATEGORY_ID);

        GameDto gameDto = new GameDto();
        gameDto.setId(EXISTS_GAME_ID);
        gameDto.setCategory(catDto);
        gameDto.setTitle(EXISTS_GAME_TITLE);

        LoanDto loanDto = new LoanDto();
        loanDto.setClient(clientDto);
        loanDto.setGame(gameDto);
        loanDto.setInitialDate(new Date(122, 11, 15));
        loanDto.setFinalDate(new Date(122, 11, 20));

        Client client = mock(Client.class);
        Game game = mock(Game.class);
        List<Game> gameList = new ArrayList<Game>();
        gameList.add(game);
        List<Loan> emptyList = new ArrayList<Loan>();
        List<Loan> notEmptyList = new ArrayList<Loan>();
        Loan loan = mock(Loan.class);
        notEmptyList.add(loan);

        when(loanRepository.checkClientHasLoan(EXISTS_CLIENT_ID, loanDto.getInitialDate(), loanDto.getFinalDate()))
                .thenReturn(notEmptyList);

        try {
            loanService.save(loanDto);
            fail("Este metodo debería fallar insertando otro nombre igual");
        } catch (Exception exception) {
            ;
        }
    }

    @Test
    public void saveWithGameInOtherLoanShouldException() {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(EXISTS_CLIENT_ID);

        CategoryDto catDto = new CategoryDto();
        catDto.setName(EXISTS_CATEGORY_NAME);
        catDto.setId(EXISTS_CATEGORY_ID);

        GameDto gameDto = new GameDto();
        gameDto.setId(EXISTS_GAME_ID);
        gameDto.setCategory(catDto);
        gameDto.setTitle(EXISTS_GAME_TITLE);

        LoanDto loanDto = new LoanDto();
        loanDto.setClient(clientDto);
        loanDto.setGame(gameDto);
        loanDto.setInitialDate(new Date(122, 11, 15));
        loanDto.setFinalDate(new Date(122, 11, 20));

        Client client = mock(Client.class);
        Game game = mock(Game.class);
        List<Game> gameList = new ArrayList<Game>();
        gameList.add(game);
        List<Loan> emptyList = new ArrayList<Loan>();
        List<Loan> notEmptyList = new ArrayList<Loan>();
        Loan loan = mock(Loan.class);
        notEmptyList.add(loan);

        when(loanRepository.checkClientHasLoan(EXISTS_CLIENT_ID, loanDto.getInitialDate(), loanDto.getFinalDate()))
                .thenReturn(emptyList);
        when(loanRepository.checkGameInLoan(EXISTS_GAME_ID, loanDto.getInitialDate(), loanDto.getFinalDate()))
                .thenReturn(notEmptyList);

        try {
            loanService.save(loanDto);
            fail("Este metodo debería fallar insertando otro nombre igual");
        } catch (Exception exception) {
            ;
        }
    }

    public static final Long EXISTS_LOAN_ID = 1L;

    @Test
    public void deleteExistsCategoryIdShouldDelete() {

        loanService.delete(EXISTS_LOAN_ID);

        verify(loanRepository).deleteById(EXISTS_CATEGORY_ID);
    }

}

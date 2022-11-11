package com.ccsw.tutorial.loan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.ccsw.tutorial.author.model.AuthorDto;
import com.ccsw.tutorial.category.model.CategoryDto;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoanIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/loan/";

    public static final String SEARCH_BY_CLIENT_PATH = "client_id=";
    public static final int EXISTS_CLIENT_ID = 1;

    public static final String SEARCH_BY_GAME_PATH = "game_id=";
    public static final int EXISTS_GAME_ID = 1;

    public static final String SEARCH_BY_DATE_PATH = "date=";
    public static final String EXISTS_DATE = "05-11-2022";

    public static final int NOT_EXISTS_CLIENT_ID = 20;
    public static final int NOT_EXISTS_GAME_ID = 20;
    public static final String NOT_EXISTS_DATE = "23-11-2022";

    public static final int PAGE_SIZE = 5;
    public static final int TOTAL_LOANS = 8;
    public static final Long EXISTS_LOAN_ID = 1L;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<Page<LoanDto>> responseTypePage = new ParameterizedTypeReference<Page<LoanDto>>() {
    };

    @Test
    public void findFirstPageWithFiveSizeShouldReturnFirstFiveResults() {

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(PageRequest.of(0, PAGE_SIZE));

        ResponseEntity<Page<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST,
                new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());
    }

    @Test
    public void findSecondPageWithFiveSizeShouldReturnLastResult() {

        int elementsCount = TOTAL_LOANS - PAGE_SIZE;

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(PageRequest.of(1, PAGE_SIZE));

        ResponseEntity<Page<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST,
                new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());
        assertEquals(elementsCount, response.getBody().getContent().size());
    }

    @Test
    public void findExistsClientShouldReturnPageOfLoans() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(PageRequest.of(1, PAGE_SIZE));

        ResponseEntity<Page<LoanDto>> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + '?' + SEARCH_BY_CLIENT_PATH + EXISTS_CLIENT_ID, HttpMethod.POST,
                new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(3, response.getBody().getTotalElements());
    }

    @Test
    public void findExistsGameShouldReturnPageOfLoans() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(PageRequest.of(1, PAGE_SIZE));

        ResponseEntity<Page<LoanDto>> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + '?' + SEARCH_BY_GAME_PATH + EXISTS_GAME_ID, HttpMethod.POST,
                new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    public void findExistsDateShouldReturnPageOfLoans() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(PageRequest.of(1, PAGE_SIZE));

        ResponseEntity<Page<LoanDto>> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + '?' + SEARCH_BY_DATE_PATH + EXISTS_DATE, HttpMethod.POST,
                new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(6, response.getBody().getTotalElements());
    }

    @Test
    public void findExistsClienteAndGameShouldReturnPageOfLoans() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(PageRequest.of(1, PAGE_SIZE));

        ResponseEntity<Page<LoanDto>> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + '?' + SEARCH_BY_CLIENT_PATH + EXISTS_CLIENT_ID + "&"
                        + SEARCH_BY_GAME_PATH + EXISTS_GAME_ID,
                HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    public void findExistsClienteAndDateShouldReturnPageOfLoans() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(PageRequest.of(1, PAGE_SIZE));

        ResponseEntity<Page<LoanDto>> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + '?' + SEARCH_BY_CLIENT_PATH + EXISTS_CLIENT_ID + "&"
                        + SEARCH_BY_DATE_PATH + EXISTS_DATE,
                HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    public void findExistsGameAndDateShouldReturnPageOfLoans() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(PageRequest.of(1, PAGE_SIZE));

        ResponseEntity<Page<LoanDto>> response = restTemplate
                .exchange(
                        LOCALHOST + port + SERVICE_PATH + '?' + SEARCH_BY_GAME_PATH + EXISTS_GAME_ID + "&"
                                + SEARCH_BY_DATE_PATH + EXISTS_DATE,
                        HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    public void findNotExistsClientShouldReturnEmpty() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(PageRequest.of(1, PAGE_SIZE));

        ResponseEntity<Page<LoanDto>> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + '?' + SEARCH_BY_CLIENT_PATH + NOT_EXISTS_CLIENT_ID, HttpMethod.POST,
                new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(0, response.getBody().getTotalElements());
    }

    @Test
    public void findNotExistsGameShouldReturnEmpty() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(PageRequest.of(1, PAGE_SIZE));

        ResponseEntity<Page<LoanDto>> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + '?' + SEARCH_BY_GAME_PATH + NOT_EXISTS_GAME_ID, HttpMethod.POST,
                new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(0, response.getBody().getTotalElements());

    }

    @Test
    public void findNotExistsDateShouldReturnEmpty() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(PageRequest.of(1, PAGE_SIZE));

        ResponseEntity<Page<LoanDto>> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + '?' + SEARCH_BY_DATE_PATH + NOT_EXISTS_DATE, HttpMethod.POST,
                new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(0, response.getBody().getTotalElements());
    }

    @Test
    public void findNotExistsTitleOrCategoryOrDateShouldReturnEmpty() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(PageRequest.of(1, PAGE_SIZE));

        ResponseEntity<Page<LoanDto>> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + '?' + SEARCH_BY_GAME_PATH + NOT_EXISTS_GAME_ID + "&"
                        + SEARCH_BY_DATE_PATH + EXISTS_DATE,
                HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(0, response.getBody().getTotalElements());
    }

    @Test
    public void saveWithoutIdShouldCreateNewLoan() {
        String searchDate = "06-12-2022";
        LoanDto dto = new LoanDto();

        ClientDto client = new ClientDto();
        client.setId(new Long(1));
        client.setName("Kaladin");

        AuthorDto author = new AuthorDto();
        author.setId(new Long(1));
        author.setName("Vital Lacerda");
        author.setNationality("PT");

        CategoryDto cat = new CategoryDto();
        cat.setId(new Long(1));
        cat.setName("Eurogames");

        GameDto game = new GameDto();
        game.setAge("14");
        game.setAuthor(author);
        game.setCategory(cat);
        game.setId(new Long(1));
        game.setTitle("On Mars");

        Date initial_date = new Date(122, 11, 01);
        Date final_date = new Date(122, 11, 07);

        dto.setClient(client);
        dto.setGame(game);
        dto.setInitialDate(initial_date);
        dto.setFinalDate(final_date);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(PageRequest.of(1, PAGE_SIZE));
        ResponseEntity<Page<LoanDto>> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + '?' + SEARCH_BY_DATE_PATH + searchDate, HttpMethod.POST,
                new HttpEntity<>(searchDto), responseTypePage);

        assertEquals(1, response.getBody().getTotalElements());

    }

    @Test
    public void saveWithFinalBeforeInitialShouldException() {

        LoanDto dto = new LoanDto();

        ClientDto client = new ClientDto();
        client.setId(new Long(1));
        client.setName("Kaladin");

        AuthorDto author = new AuthorDto();
        author.setId(new Long(1));
        author.setName("Vital Lacerda");
        author.setNationality("PT");

        CategoryDto cat = new CategoryDto();
        cat.setId(new Long(1));
        cat.setName("Eurogames");

        GameDto game = new GameDto();
        game.setAge("14");
        game.setAuthor(author);
        game.setCategory(cat);
        game.setId(new Long(1));
        game.setTitle("On Mars");

        Date initial_date = new Date(122, 10, 15);
        Date final_date = new Date(122, 10, 05);

        dto.setClient(client);
        dto.setGame(game);
        dto.setInitialDate(initial_date);
        dto.setFinalDate(final_date);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT,
                new HttpEntity<>(dto), Void.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void saveWithMoreThanMaxDaysShouldException() {
        LoanDto dto = new LoanDto();

        ClientDto client = new ClientDto();
        client.setId(new Long(1));
        client.setName("Kaladin");

        AuthorDto author = new AuthorDto();
        author.setId(new Long(1));
        author.setName("Vital Lacerda");
        author.setNationality("PT");

        CategoryDto cat = new CategoryDto();
        cat.setId(new Long(1));
        cat.setName("Eurogames");

        GameDto game = new GameDto();
        game.setAge("14");
        game.setAuthor(author);
        game.setCategory(cat);
        game.setId(new Long(1));
        game.setTitle("On Mars");

        Date initial_date = new Date(122, 10, 13);
        Date final_date = new Date(122, 10, 30);

        dto.setClient(client);
        dto.setGame(game);
        dto.setInitialDate(initial_date);
        dto.setFinalDate(final_date);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT,
                new HttpEntity<>(dto), Void.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void saveWithSameDatesOfOtheroLoanShouldException() {
        LoanDto dto = new LoanDto();

        ClientDto client = new ClientDto();
        client.setId(new Long(10));
        client.setName("JFDO");

        AuthorDto author = new AuthorDto();
        author.setId(new Long(1));
        author.setName("Vital Lacerda");
        author.setNationality("PT");

        CategoryDto cat = new CategoryDto();
        cat.setId(new Long(1));
        cat.setName("Eurogames");

        GameDto game = new GameDto();
        game.setAge("14");
        game.setAuthor(author);
        game.setCategory(cat);
        game.setId(new Long(1));
        game.setTitle("On Mars");

        Date initial_date = new Date(122, 10, 06);
        Date final_date = new Date(122, 10, 15);

        dto.setClient(client);
        dto.setGame(game);
        dto.setInitialDate(initial_date);
        dto.setFinalDate(final_date);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT,
                new HttpEntity<>(dto), Void.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void saveWithTwoGamesInOneDayShouldException() {
        LoanDto dto = new LoanDto();

        ClientDto client = new ClientDto();
        client.setId(new Long(1));
        client.setName("Kaladin");

        AuthorDto author = new AuthorDto();
        author.setId(new Long(1));
        author.setName("Vital Lacerda");
        author.setNationality("PT");

        CategoryDto cat = new CategoryDto();
        cat.setId(new Long(1));
        cat.setName("Eurogames");

        GameDto game = new GameDto();
        game.setAge("14");
        game.setAuthor(author);
        game.setCategory(cat);
        game.setId(new Long(10));
        game.setTitle("Onfdskl");

        Date initial_date = new Date(122, 10, 02);
        Date final_date = new Date(122, 10, 13);

        dto.setClient(client);
        dto.setGame(game);
        dto.setInitialDate(initial_date);
        dto.setFinalDate(final_date);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT,
                new HttpEntity<>(dto), Void.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void deleteWithNotExistsIdShouldThrowException() {

        long deleteLoanId = TOTAL_LOANS + 1;

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + deleteLoanId,
                HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void deleteWithExistsIdShouldDelete() {

        long newTotalLoans = TOTAL_LOANS - 1;
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(PageRequest.of(0, PAGE_SIZE));

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + EXISTS_LOAN_ID, HttpMethod.DELETE, null, Void.class);
        ResponseEntity<Page<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST,
                new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(newTotalLoans, response.getBody().getTotalElements());
    }

}

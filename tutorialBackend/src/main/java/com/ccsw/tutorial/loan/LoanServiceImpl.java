package com.ccsw.tutorial.loan;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ccsw.tutorial.category.CategoryService;
import com.ccsw.tutorial.client.ClientService;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.loan.exceptions.FinalBeforeInitialException;
import com.ccsw.tutorial.loan.exceptions.GameInLoanException;
import com.ccsw.tutorial.loan.exceptions.MaxDaysException;
import com.ccsw.tutorial.loan.exceptions.MaxLoansException;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientService clientService;

    @Autowired
    GameService gameService;

    @Autowired
    CategoryService categoryService;

    @Override
    public Page<Loan> find(Long clientID, Long gameID, Date date, LoanSearchDto dto) {
        // Esto quiza mejor todo en una funcion ya que al final devuelve una pagina,
        // filtrada o no
        if ((clientID == null) && (gameID == null) && (date == null))
            return this.loanRepository.findAll(dto.getPageable());
        else
            return this.loanRepository.findBy(clientID, gameID, date, dto.getPageable());
    }

    @Override
    public void save(LoanDto dto) throws Exception {

        /*
         * La fecha de fin NO podrá ser anterior a la fecha de inicio
         * 
         * El periodo de préstamo máximo solo podrá ser de 14 días. Si el usuario quiere
         * un préstamo para más de 14 días la aplicación no debe permitirlo mostrando
         * una alerta al intentar guardar.
         * 
         * El mismo juego no puede estar prestado a dos clientes distintos en un mismo
         * día. OJO que los préstamos tienen fecha de inicio y fecha fin, el juego no
         * puede estar prestado a más de un cliente para ninguno de los días que
         * contemplan las fechas actuales del formulario.
         * 
         * -> select * from Loan l where (l.game_id = :gameID) AND (l.initial_date
         * BETWEEN :initial_date AND :final_date) AND (l.final_date BETWEEN
         * :initial_date AND :final_date)
         * 
         * 
         * Un mismo cliente no puede tener prestados más de 2 juegos en un mismo día.
         * OJO que los préstamos tienen fecha de inicio y fecha fin, el cliente no puede
         * tener más de dos préstamos para ninguno de los días que contemplan las fechas
         * actuales del formulario.
         * 
         * -> select * from Loan l where (l.client_id = :clientID) AND (l.initial_date
         * BETWEEN :initial_date AND :final_date) AND (l.final_date BETWEEN
         * :initial_date AND :final_date)
         * 
         * MEJOR USAR LAS DOS CONSULTAS X SEPARADO EN VEZ DE ESTA, ASI PODEMOS
         * DIFERENCIAR ERRORES select * from Loan l where ((l.client_id = :clientID) OR
         * (l.game_id = :gameID)) AND (l.initial_date BETWEEN :initial_date AND
         * :final_date) AND (l.final_date BETWEEN :initial_date AND :final_date)
         */

        if (dto.getFinalDate().before(dto.getInitialDate()))
            throw new FinalBeforeInitialException("Bad date, final before initial");

        Long diffBetweenDates = dto.getFinalDate().getTime() - dto.getInitialDate().getTime();
        double loanDays = diffBetweenDates / (1000 * 60 * 60 * 24);
        if (loanDays > 14)
            throw new MaxDaysException("Max loan time is 14 days");

        List<Loan> checks = this.loanRepository.checkClientHasLoan(dto.getClient().getId(), dto.getInitialDate(),
                dto.getFinalDate());
        if (checks.size() > 0)
            throw new MaxLoansException("Client has loan between the dates");

        checks = null;
        checks = this.loanRepository.checkGameInLoan(dto.getGame().getId(), dto.getInitialDate(), dto.getFinalDate());
        if (checks.size() > 0)
            throw new GameInLoanException("Game in loan between the dates");

        Loan loan = new Loan();
        loan.setClient(this.clientService.get(dto.getClient().getId()));

        // esto es un poco meh
        List<Game> game = this.gameService.find(dto.getGame().getTitle(), dto.getGame().getCategory().getId());
        loan.setGame(game.get(0));

        loan.setInitialDate(dto.getInitialDate());
        loan.setFinalDate(dto.getFinalDate());

        this.loanRepository.save(loan);

    }

    @Override
    public void delete(Long id) {
        this.loanRepository.deleteById(id);
    }
}
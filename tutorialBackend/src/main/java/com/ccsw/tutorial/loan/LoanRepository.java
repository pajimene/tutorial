package com.ccsw.tutorial.loan;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ccsw.tutorial.loan.model.Loan;

/**
 * @author ccsw
 */
public interface LoanRepository extends CrudRepository<Loan, Long> {

    /**
     * Método para recuperar un listado paginado de
     * {@link com.ccsw.tutorial.loan.model.Loan}
     * 
     * @param page
     * @return
     */
    Page<Loan> findAll(Pageable pageable);

    @Query(value = "select * from Loan l where ((:clientID is null OR l.client_id like :clientID) AND (:gameID is null or l.game_id like :gameID) "
            + "AND (:date is null or :date BETWEEN l.initial_date AND l.final_date) )", nativeQuery = true)
    Page<Loan> findBy(@Param("clientID") Long clientID, @Param("gameID") Long gameID, @Param("date") Date date,
            Pageable dto);

    @Query(value = "select * from Loan l where (l.game_id = :gameID) "
            + "AND ((l.initial_date BETWEEN :initial_date AND :final_date) "
            + "OR (l.final_date BETWEEN :initial_date AND :final_date))", nativeQuery = true)
    List<Loan> checkGameInLoan(@Param("gameID") Long gameID, @Param("initial_date") Date initial_date,
            @Param("final_date") Date final_date);

    @Query(value = "select * from Loan l where (l.client_id = :clientID) "
            + "AND ((l.initial_date BETWEEN :initial_date AND :final_date) "
            + "OR (l.final_date BETWEEN :initial_date AND :final_date))", nativeQuery = true)
    List<Loan> checkClientHasLoan(@Param("clientID") Long clientID, @Param("initial_date") Date initial_date,
            @Param("final_date") Date final_date);

}

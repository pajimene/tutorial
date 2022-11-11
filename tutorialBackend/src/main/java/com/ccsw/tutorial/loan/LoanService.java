package com.ccsw.tutorial.loan;

import java.util.Date;

import org.springframework.data.domain.Page;

import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;

/**
 * @author ccsw
 */
public interface LoanService {

    /**
     * Método para recuperar un listado paginado (filtrado o no) de
     * {@link com.ccsw.tutorial.author.model.Author}
     * 
     * @param clientID
     * @param gameID
     * @param date
     * @param dto
     * @return
     */
    Page<Loan> find(Long clientID, Long gameID, Date date, LoanSearchDto dto);

    /**
     * Método para guardar un {@link com.ccsw.tutorial.loan.model.Loan}
     * 
     * @param dto
     * @return void
     */
    void save(LoanDto dto) throws Exception;

    /**
     * Método para borrar un {@link com.ccsw.tutorial.loan.model.Loan}
     * 
     * @param id
     * @return void
     */
    void delete(Long id);
}

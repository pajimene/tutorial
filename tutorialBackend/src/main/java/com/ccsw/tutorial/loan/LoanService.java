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
     * Método para recuperar un listado paginado de
     * {@link com.ccsw.tutorial.author.model.Author}
     * 
     * @param dto
     * @return
     */

    Page<Loan> find(Long clientID, Long gameID, Date date, LoanSearchDto dto);

    void save(LoanDto dto) throws Exception;

    void delete(Long id);
}

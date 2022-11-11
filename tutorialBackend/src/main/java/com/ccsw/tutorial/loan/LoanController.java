package com.ccsw.tutorial.loan;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccsw.tutorial.config.mapper.BeanMapper;
import com.ccsw.tutorial.loan.exceptions.FinalBeforeInitialException;
import com.ccsw.tutorial.loan.exceptions.GameInLoanException;
import com.ccsw.tutorial.loan.exceptions.MaxDaysException;
import com.ccsw.tutorial.loan.exceptions.MaxLoansException;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;

/**
 * @author ccsw
 */
@RequestMapping(value = "/loan")
@RestController
@CrossOrigin(origins = "*")
public class LoanController {

    @Autowired
    LoanService loanService;

    @Autowired
    BeanMapper beanMapper;

    /**
     * Método para recuperar un listado paginado de
     * {@link com.ccsw.tutorial.loan.model.Loan}
     * 
     * @param dto
     * @return
     */

    @RequestMapping(path = "", method = RequestMethod.POST)
    public Page<LoanDto> find(@RequestParam(value = "client_id", required = false) Long clientID,
            @RequestParam(value = "game_id", required = false) Long gameID,
            @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date date,
            @RequestBody LoanSearchDto dto) {

        Page<Loan> loans = this.loanService.find(clientID, gameID, date, dto);
        return beanMapper.mapPage(loans, LoanDto.class);
    }

    @RequestMapping(path = "", method = RequestMethod.PUT)
    public void save(@RequestBody LoanDto dto) throws Exception {
        try {
            this.loanService.save(dto);
        } catch (MaxDaysException e) {
            throw new MaxDaysException(e.getMessage());
        } catch (MaxLoansException e) {
            throw new MaxLoansException(e.getMessage());
        } catch (FinalBeforeInitialException e) {
            throw new FinalBeforeInitialException(e.getMessage());
        } catch (GameInLoanException e) {
            throw new GameInLoanException(e.getMessage());
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        this.loanService.delete(id);
    }

}

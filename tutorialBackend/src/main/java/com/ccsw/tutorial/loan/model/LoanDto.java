package com.ccsw.tutorial.loan.model;

import java.util.Date;

import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.game.model.GameDto;

/**
 * @author ccsw
 */
public class LoanDto {

    private Long id;

    private ClientDto client;

    private GameDto game;

    private Date initialDate;

    private Date finalDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClientDto getClient() {
        return client;
    }

    public void setClient(ClientDto client) {
        this.client = client;
    }

    public GameDto getGame() {
        return game;
    }

    public void setGame(GameDto game) {
        this.game = game;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initial_date) {
        this.initialDate = initial_date;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    /**
     * @param author new value of {@link #getAuthor}.
     */
    public void setFinalDate(Date final_date) {
        this.finalDate = final_date;
    }

}

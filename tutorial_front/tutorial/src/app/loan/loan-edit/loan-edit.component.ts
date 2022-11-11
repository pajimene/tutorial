import { Component, Inject, ViewChild, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { LoanService } from '../loan.service';
import { Loan } from '../model/Loan';
import { Client } from 'src/app/client/model/Client';
import { Game } from 'src/app/game/model/Game';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-loan-edit',
  templateUrl: './loan-edit.component.html',
  styleUrls: ['./loan-edit.component.scss']
})

export class LoanEditComponent implements OnInit {

    loan : Loan;
    clients : Client[];
    games : Game[];

    selectedClient : Client;
    selectedGame : Game;
    selectedInitialDate : Date;
    selectedFinalDate : Date;

    errMessage : String = null;

    constructor(
        public dialogRef: MatDialogRef<LoanEditComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any,
        private loanService: LoanService
        ) { }

    ngOnInit(): void {
        this.loan = new Loan();
        this.clients = this.data.clients; 
        this.games = this.data.games; 
    }

    setClientSelected(client: Client){
        this.selectedClient = client;
    } 
    setGameSelected(game: Game){
        this.selectedGame = game;
    }
    setSelectedInitialDate(initial_date: Date){
        this.selectedInitialDate = initial_date;
    }
    setSelectedFinalDate(final_date: Date){
        this.selectedFinalDate = final_date;
    }

    onSave() {
        this.loan = new Loan();

        this.loan.client = this.selectedClient;
        this.loan.game = this.selectedGame;
        this.loan.initialDate = this.selectedInitialDate;
        this.loan.finalDate = this.selectedFinalDate;

        let error = this.checkData(this.loan);
        if(error == null)
            this.loanService.saveLoan(this.loan).subscribe(
                data =>  this.dialogRef.close(),
                err => this.setErrorMessage(err)
            ); 
        else
            this.errMessage = error;
    }  

    setErrorMessage(error : HttpErrorResponse)
    {
        this.errMessage = error.error.message;
    }

    onClose() {
        this.dialogRef.close();
    }

    checkData(loan : Loan) : String
    {
        if(loan.initialDate > loan.finalDate)
            return "Initial date can't be latter than final";

        let time : number = loan.finalDate.getTime() - loan.initialDate.getTime();
        let loanDays : number = time / (1000 * 60 * 60 * 24);
        console.log(loanDays)
        if(loanDays > 14)
            return "More than 14 days";

        return null;
    }
}



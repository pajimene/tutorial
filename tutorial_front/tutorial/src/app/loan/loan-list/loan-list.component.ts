import { Component, OnInit } from '@angular/core';
import { LoanService } from '../loan.service';
import { MatDialog } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { Pageable } from 'src/app/core/model/page/Pageable';
import { MatTableDataSource } from '@angular/material/table';
import { Loan } from '../model/Loan';
import { LoanEditComponent } from '../loan-edit/loan-edit.component';
import { DialogConfirmationComponent } from 'src/app/core/dialog-confirmation/dialog-confirmation.component';
import { ClientService } from 'src/app/client/client.service';
import { GameService } from 'src/app/game/game.service';
import { Client } from 'src/app/client/model/Client';
import { Game } from 'src/app/game/model/Game';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';
import { throwDialogContentAlreadyAttachedError } from '@angular/cdk/dialog';

@Component({
  selector: 'app-loan-list',
  templateUrl: './loan-list.component.html',
  styleUrls: ['./loan-list.component.scss']
})
export class LoanListComponent implements OnInit {

  pageNumber: number = 0;
  pageSize: number = 5;
  totalElements: number = 0;
  clients : Client[];
  games : Game[];

  selectedClientName : string;
  selectedClientId : number;

  selectedGameTitle : string;
  selectedGameId : number;

  filterDate : Date;

  errorMessage : String;


  dataSource = new MatTableDataSource<Loan>();
  displayedColumns: string[] = ['id', 'client', 'game', 'initialDate', 'finalDate', 'action'];


  constructor( 
        private loanService: LoanService,
        private clientService : ClientService,
        private gameService : GameService,
        public dialog: MatDialog,
      ) { }

  ngOnInit(): void {
    this.loadClients();
    this.loadGames();
    this.loadPage();
  }

  loadClients(){
    this.clientService.getClients().subscribe(
      clients => this.clients = clients
    );
  }
  loadGames(){
    this.gameService.getGames().subscribe(
      games => this.games = games
    );
  }

  dateFilterChange(event: MatDatepickerInputEvent<Date>) {
    this.filterDate = event.value;
  }

  loadPage(event?: PageEvent) {

    let pageable : Pageable =  {
        pageNumber: this.pageNumber,
        pageSize: this.pageSize,
        sort: [{
            property: 'id',
            direction: 'ASC'
        }]
    }
    if (event != null) {
        pageable.pageSize = event.pageSize
        pageable.pageNumber = event.pageIndex;
    }
    this.loanService.getLoans(pageable).subscribe(data => {
        this.dataSource.data = data.content;  
        this.pageNumber = data.pageable.pageNumber;
        this.pageSize = data.pageable.pageSize;
        this.totalElements = data.totalElements;
    });
  } 

  createLoan() {      
    const dialogRef = this.dialog.open(LoanEditComponent, {
      data: { 'clients' : this.clients, 'games' : this.games}
  });
  dialogRef.afterClosed().subscribe(result => {
      this.ngOnInit();
  }); 
}  

deleteLoan(loan: Loan) {    
  const dialogRef = this.dialog.open(DialogConfirmationComponent, {
    data: { title: "Eliminar préstamo", description: "Atención si borra el préstamo se perderán sus datos.<br> ¿Desea eliminar el préstamo?" }
});

dialogRef.afterClosed().subscribe(result => {
    if (result) {
        this.loanService.deleteLoan(loan.id).subscribe(result =>  {
            this.ngOnInit();
        }); 
    }
});
}  

onCleanFilter(){
  this.selectedClientId = null;
  this.selectedClientName = null;
  this.selectedGameId = null;
  this.selectedGameTitle = null;
  this.filterDate = null;
  this.errorMessage = null;
  this.loadPage();
}

onSearch(){
  let pageable : Pageable =  {
    pageNumber: this.pageNumber,
    pageSize: this.pageSize,
    sort: [{
        property: 'id',
        direction: 'ASC'
    }]
}

  this.loanService.searchLoans(pageable, this.selectedClientId, this.selectedGameId,  this.filterDate )
 .subscribe(data =>{  
      this.dataSource.data = data.content;
      this.pageNumber = data.pageable.pageNumber;
      this.pageSize = data.pageable.pageSize;
      this.totalElements = data.totalElements;
    } )
}

setClientSelected(id : number)
{
  this.selectedClientId = id;
}

setGameSelected(id : number)
{
  this.selectedGameId = id;
}


}

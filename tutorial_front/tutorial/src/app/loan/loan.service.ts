import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Pageable } from '../core/model/page/Pageable';
import { Loan } from './model/Loan';
import { LoanPage } from './model/LoanPage';
import { LOAN_DATA } from './model/mock-loans';
import { HttpClient } from '@angular/common/http';
import { formatDate } from '@angular/common';

@Injectable({
    providedIn: 'root'
})
export class LoanService {

    constructor( 
        private http: HttpClient
        ) { }

    getLoans(pageable: Pageable): Observable<LoanPage> {
        return this.http.post<LoanPage>('http://localhost:8080/loan', {pageable:pageable});
    }

    searchLoans( pageable: Pageable, clientFilter : number, gameFilter : number, dateFilter : Date): Observable<LoanPage>
    {
        let url = 'http://localhost:8080/loan';
        let primero = true;
        let miFecha : String;
        if(dateFilter != null)
            miFecha = dateFilter.getDate() + '-' + (dateFilter.getMonth()+1) + '-' + dateFilter.getFullYear();
       
        if(clientFilter)
        {
            url+='?client_id='+ clientFilter;
            primero = false;
        }  
        if(gameFilter)
            if(!primero)
                url+='&game_id='+ gameFilter;
            else
            {
                url+='?game_id='+ gameFilter;
                primero = false;
            }
        if(dateFilter)
            if(!primero)
                url+='&date='+ miFecha;
            else
                url += '?date=' + miFecha;

        return this.http.post<LoanPage>(url, {pageable:pageable});
    }

    saveLoan(loan: Loan): Observable<void> {
        return this.http.put<void>('http://localhost:8080/loan', loan);
    }

    deleteLoan(idLoan : number): Observable<void> {
        return this.http.delete<void>('http://localhost:8080/loan/' + idLoan);
    }    
}
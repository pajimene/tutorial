import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ClientService } from '../client.service';
import { Client } from '../model/Client';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-client-edit',
  templateUrl: './client-edit.component.html',
  styleUrls: ['./client-edit.component.scss']
})
export class ClientEditComponent implements OnInit {

  client : Client;
  sameNameAdvice : String;

  constructor(
    public dialogRef: MatDialogRef<ClientEditComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private clientService: ClientService
  ) { }

  ngOnInit(): void {
    if (this.data.client != null) {
      this.client = Object.assign({}, this.data.client);
    }
    else {
      this.client = new Client();
    }
  }

  onSave() {
   this.clientService.saveClient(this.client).subscribe(
      data => this.dialogRef.close(),
      err => this.setSameNameAdvice(err),
  );
  }  

  onClose() {
    this.dialogRef.close();
  }
  
  setSameNameAdvice(err : HttpErrorResponse){
    if(err.status == 409)
      this.sameNameAdvice = err.error.message;
  }
}


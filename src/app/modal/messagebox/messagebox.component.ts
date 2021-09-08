import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-messagebox',
  templateUrl: './messagebox.component.html',
  styleUrls: ['./messagebox.component.css']
})
export class MessageboxComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<MessageboxComponent>,
    @Inject(MAT_DIALOG_DATA) public data) { }

  ngOnInit(): void {
  }

}

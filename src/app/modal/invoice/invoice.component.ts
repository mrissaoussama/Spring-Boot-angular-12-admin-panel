import { Category } from '../../models/category.model';
import { ProductService } from '../../_services/product.service';
import { Component, OnInit, Inject, Optional, ElementRef, ViewChild } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Product } from 'src/app/models/product.model';
import { MatDialog } from '@angular/material/dialog';
import { MatSelect } from '@angular/material/select';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';
@Component({
  selector: 'app-invoice',
  templateUrl: './invoice.component.html',
  styleUrls: ['./invoice.component.css']
})
export class InvoiceComponent implements OnInit {

  constructor(@Optional() public dialogRef?: MatDialogRef<InvoiceComponent>, @Optional()
  @Inject(MAT_DIALOG_DATA) public data?: any) { }
  subtotal: number = 0;
  total: number = 0;
  shipping: number = 15;
  ngOnInit(): void {

    this.calculatetotal()
  }
  calculatetotal() {
    this.data.shoppingcart.cartItems.forEach(element => {
      this.subtotal += element.product.price * element.quantity



    });
    this.total = this.subtotal + this.shipping
  }
  onBtnPrintClick() {
    let printData = document.getElementById('dataToPrint').cloneNode(true);
    document.body.appendChild(printData);
    window.print();
    document.body.removeChild(printData);
  }
  @ViewChild('dataToPrint', { static: false }) dataToPrint: ElementRef;

  public downloadAsPDF() {
    const DATA = document.getElementById("dataToPrint")

    html2canvas(DATA).then(canvas => {

      let fileWidth = 208;
      let fileHeight = canvas.height * fileWidth / canvas.width;

      const FILEURI = canvas.toDataURL('image/png')
      let PDF = new jsPDF('p', 'mm', 'a4');
      let position = 0;
      PDF.addImage(FILEURI, 'PNG', 0, position, fileWidth, fileHeight)

      PDF.save('Order N ' + this.data.shoppingcart.id + '.pdf');
    });
  }
}

import { Component, Inject, OnInit, Optional } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Cartitem } from 'src/app/models/cartitem.model';
import { ProductService } from 'src/app/_services/product.service';
import { Shoppingcart } from './../../models/shoppingcart.model';
@Component({
  selector: 'app-editshoppingcart',
  templateUrl: './editshoppingcart.component.html',
  styleUrls: ['./editshoppingcart.component.css']
})
export class EditshoppingcartComponent implements OnInit {

  constructor(public productservice: ProductService, @Optional() public dialogRef?: MatDialogRef<EditshoppingcartComponent>, @Optional()
  @Inject(MAT_DIALOG_DATA) public data?: any) { }
  subtotal: number = 0;
  total: number = 0;
  shipping: number = 15;
  cartitemstoremove: Cartitem[] = [];
  shoppingcart: Shoppingcart
  ngOnInit(): void {

    this.shoppingcart = JSON.parse(JSON.stringify(this.data.shoppingcart))
    this.calculatetotal()
  }
  save(): void {
    if (this.cartitemstoremove.length != 0)
      this.cartitemstoremove.forEach(element => {
        this.productservice.removeCartItem(this.shoppingcart.user.id, element.product.id).subscribe(
          data => {
          },
          err => {
            console.log(err)
          })
      });

    this.productservice.updateShoppingCartItems(this.shoppingcart.user.id, this.shoppingcart.cartItems).subscribe(
      data => {
      },
      err => {
        console.log(err)
      })
    return this.dialogRef.close();
  }
  changecartitemquantity(cartitem: Cartitem, $event) {
    cartitem.quantity = $event.target.value;
    this.calculatetotal()
  }
  calculatetotal() {
    this.subtotal = 0;
    this.total = 0;
    this.shoppingcart.cartItems.forEach(element => {

      this.subtotal += element.product.price * element.quantity
    });
    this.total = this.subtotal + this.shipping
  }
  removecartitem(cartitem: Cartitem) {
    this.cartitemstoremove.push(cartitem)
    this.shoppingcart.cartItems.forEach((item, index) => {
      if (item === cartitem)
        this.shoppingcart.cartItems.splice(index, 1);
      this.calculatetotal()

    });
  }


}

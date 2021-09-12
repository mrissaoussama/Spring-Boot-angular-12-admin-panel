import { Cartitem } from './../models/cartitem.model';
import { TokenStorageService } from 'src/app/_services/token-storage.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { catchError, map, Observable, of } from 'rxjs';
import { Category } from './../models/category.model';
import { Product } from './../models/product.model';
import { ProductRequest } from './../models/productrequest.model';
import { NumberSymbol } from '@angular/common';

const PRODUCT_API = 'http://localhost:8080/api/product/';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};
const HttpUploadOptions = {
  headers: new HttpHeaders({})
}

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  constructor(private http: HttpClient, private token: TokenStorageService) { }

  fileExists(id: any, number: any): Observable<boolean> {
    return this.http.get("/assets/Productimages/" + id + "/" + number + ".jpg").pipe(map(() => true),
      catchError(() => of(false)));
  }
  productrequest: ProductRequest = new ProductRequest('', '', 0, '', '', 0, null, false);

  updateProduct(product: Product): Observable<any> {

    var username = this.token.getUsername();
    var password = this.token.getPassword();
    this.productrequest.convert(username, password, product);
    var username = this.productrequest.username;
    var password = this.productrequest.password;
    var id = this.productrequest.id;
    var category = this.productrequest.category;
    var name = this.productrequest.name;
    var price = this.productrequest.price;
    var description = this.productrequest.description;

    return this.http.post(PRODUCT_API + 'updateProduct', {//productrequest
      id, username, password, category, price, name, description

    }, httpOptions);
  }

  addProduct(form: FormGroup): Observable<any> {
    var username = this.token.getUsername();
    var password = this.token.getPassword();
    var category = form.get('Category').value;
    var name = form.get('name').value;
    var price = form.get('price').value;
    var description = form.get('description').value;
    return this.http.post(PRODUCT_API + 'addProduct', {
      username, password, category, price,
      name
      , description
    }, httpOptions);
  }

  updateProductImage(form: FormData): Observable<any> {
    return this.http.post(PRODUCT_API + 'updateProductImage', form);
  }

  findProduct(productrequest: ProductRequest): Observable<any> {

    var username = this.token.getUsername();
    var password = this.token.getPassword();
    var id = productrequest.id;
    var category = productrequest.category;
    var name = productrequest.name;
    var price = productrequest.price;
    var description = productrequest.description;
    console.log(productrequest)

    return this.http.post(PRODUCT_API + 'findProduct', {

      id, username, password, category, price, name, description
    }, httpOptions);
  }
  deleteProduct(product: Product): Observable<any> {
    var username = this.token.getUsername();
    var password = this.token.getPassword();
    var id = product.id
    return this.http.post(PRODUCT_API + 'deleteProduct', {

      username,
      id,
      password
    }, httpOptions);
  }
  getAllProducts(): Observable<any> {

    var username = this.token.getUsername();
    var password = this.token.getPassword();
    return this.http.post(PRODUCT_API + 'findProduct', {
      username, password
    }, httpOptions);
  }
  getAllCategories(): Observable<any> {

    var username = this.token.getUsername();
    var password = this.token.getPassword();
    return this.http.post(PRODUCT_API + 'getAllCategories', {
      username, password
    }, httpOptions);
  }
  updateCategory(category: Category): Observable<any> {
    var id = category.id;
    var name = category.name
    var username = this.token.getUsername();
    var password = this.token.getPassword();
    // var price=form.get('price').value;
    // var description=form.get('description').value;
    return this.http.post(PRODUCT_API + 'updateCategory', {
      id, name,
      username, password
    }, httpOptions);
  }
  addCategory(category: Category): Observable<any> {
    var name = category.name
    var username = this.token.getUsername();
    var password = this.token.getPassword();
    // var price=form.get('price').value;
    // var description=form.get('description').value;
    return this.http.post(PRODUCT_API + 'addCategory', {
      name,
      username, password
    }, httpOptions);
  }
  deleteCategory(category: Category): Observable<any> {
    var username = this.token.getUsername();
    var password = this.token.getPassword();
    var id = category.id
    var name = category.name;
    return this.http.post(PRODUCT_API + 'deleteCategory', {
      username,
      id,
      name,
      password
    }, httpOptions);
  }
  getAllShoppingCarts(): Observable<any> {
    var username = this.token.getUsername();
    var password = this.token.getPassword();
    return this.http.post(PRODUCT_API + 'getAllShoppingCarts', {
      username, password
    }, httpOptions);
  }
  getShoppingCart(userid: any): Observable<any> {

    var username = this.token.getUsername();
    var password = this.token.getPassword();

    return this.http.post(PRODUCT_API + 'getShoppingCart', {
      username, password, userid
    }, httpOptions);
  }
  updateShoppingCartItems(userid: any, cartItems: Cartitem[]): Observable<any> {

    var username = this.token.getUsername();
    var password = this.token.getPassword();
    cartItems.forEach(element => {
      element.shoppingcart = undefined
    });
    return this.http.post(PRODUCT_API + 'updateShoppingCartItems', {
      username, password, userid, cartItems
    }, httpOptions);
  }
  removeCartItem(userid: any, productid: number): Observable<any> {

    var username = this.token.getUsername();
    var password = this.token.getPassword();

    return this.http.post(PRODUCT_API + 'removeCartItem', {
      username, password, userid, productid
    }, httpOptions);
  }
  setCompletedDate(shoppingcart: any): Observable<any> {

    var username = this.token.getUsername();
    var password = this.token.getPassword();
    var userid = shoppingcart.user.id;
    // var price=form.get('price').value;
    // var description=form.get('description').value;
    return this.http.post(PRODUCT_API + 'setCompletedDate', {
      username, password, userid
    }, httpOptions);
  }
  setShippingDate(shoppingcart: any): Observable<any> {
    var username = this.token.getUsername();
    var password = this.token.getPassword();
    var userid = shoppingcart.user.id;
    // var price=form.get('price').value;
    // var description=form.get('description').value;
    return this.http.post(PRODUCT_API + 'setShippingDate', {
      username, password, userid
    }, httpOptions);
  }
}

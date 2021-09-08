import { Category } from './../models/category.model';
import { ProductRequest } from './../models/productrequest.model';
import { Product } from './../models/product.model';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, Observable, of } from 'rxjs';
import { FormGroup } from '@angular/forms';

const AUTH_API = 'http://localhost:8080/api/product/';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};
const HttpUploadOptions = {
  headers: new HttpHeaders({ })
}

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  fileexists(id: any, number: any): Observable<boolean> {

      return this.http.get("/assets/Productimages/"+id+"/"+number+".jpg").pipe(map(() => true), catchError(() => of(false)));
    }
  productrequest:ProductRequest=new ProductRequest('','',0,'','',0,null,false);

  constructor(private http: HttpClient) { }
  updateProduct(product:Product) : Observable<any> {
    var username="mod";var password="123456";

  this.productrequest.convert(username,password,product);
    console.log(product);
    console.log(this.productrequest);
    var username=this.productrequest.username;
    var password=this.productrequest.password;
    var id=this.productrequest.id;
    var category=this.productrequest.category;
    var name=this.productrequest.name;
    var price=this.productrequest.price;
    var description=this.productrequest.description;

    return this.http.post(AUTH_API + 'updateproduct', {//productrequest
      id, username,password,category,price, name,description

    }, httpOptions);  }

    addProduct(form: FormGroup) : Observable<any> {
      var username=form.get('username').value;
      var password=form.get('password').value;
      var category=form.get('Category').value;
      var name=form.get('name').value;
      var price=form.get('price').value;
      var description=form.get('description').value;
      return this.http.post(AUTH_API + 'addproduct', {
       username,password,category,price,
       name
       ,description
      }, httpOptions);  }

      updateproductimage(form: FormData) : Observable<any> {
      return this.http.post(AUTH_API + 'updateproductimage', form);  }

  findproduct(productrequest:ProductRequest): Observable<any> {

    var username="mod";var password="123456";

    var id=productrequest.id;
    var category=productrequest.category;
    var name=productrequest.name;
    var price=productrequest.price;
    var description=productrequest.description;
console.log(productrequest)

    return this.http.post(AUTH_API + 'findproduct', {

      id, username,password,category,price, name,description
    }, httpOptions);
  }
  deleteproduct(product:Product): Observable<any> {
    var username="mod";
    var password="123456";
    var id=product.id
    return this.http.post(AUTH_API + 'deleteproduct', {

      username,
id,
      password
    }, httpOptions);
  }
  getallproducts(): Observable<any> {

    var username="mod";
    var password="123456";
   // var price=form.get('price').value;
   // var description=form.get('description').value;
    return this.http.post(AUTH_API + 'findproduct', {
username,password
    }, httpOptions);
  }
  getallcategories(): Observable<any> {

    var username="mod";
    var password="123456";
   // var price=form.get('price').value;
   // var description=form.get('description').value;
    return this.http.post(AUTH_API + 'getallcategories', {
username,password
    }, httpOptions);
  }
  updatecategory(category:Category): Observable<any> {
var id=category.id;
var name=category.name
    var username="mod";
    var password="123456";
   // var price=form.get('price').value;
   // var description=form.get('description').value;
    return this.http.post(AUTH_API + 'updatecategory', {id,name,
username,password
    }, httpOptions);
  }
  addcategory(category:Category): Observable<any> {
    var name=category.name
        var username="mod";
        var password="123456";
       // var price=form.get('price').value;
       // var description=form.get('description').value;
        return this.http.post(AUTH_API + 'addcategory', {name,
    username,password
        }, httpOptions);
      }
  deletecategory(category:Category): Observable<any> {
    var username="mod";
    var password="123456";
    var id=category.id
    var name=category.name;
    return this.http.post(AUTH_API + 'deletecategory', {
 username,
id,
name,
      password
    }, httpOptions);
  }
  getallshoppingcarts(): Observable<any> {

    var username="mod";
    var password="123456";
   // var price=form.get('price').value;
   // var description=form.get('description').value;
    return this.http.post(AUTH_API + 'getallshoppingcarts', {
username,password
    }, httpOptions);
  }
  getshoppingcart(): Observable<any> {

    var username="mod";
    var password="123456";
    var userid=1
   // var price=form.get('price').value;
   // var description=form.get('description').value;
    return this.http.post(AUTH_API + 'getshoppingcart', {
username,password,userid
    }, httpOptions);
  }
  setcompleteddate(completeddate:Date): Observable<any> {

    var username="mod";
    var password="123456";
    var userid=1
   // var price=form.get('price').value;
   // var description=form.get('description').value;
    return this.http.post(AUTH_API + 'setcompleteddate', {
username,password,userid
    }, httpOptions);
  }
  setshippingdate(shoppingcart): Observable<any> {
console.log(shoppingcart)
    var username="mod";
    var password="123456";
    var userid=shoppingcart.user.id;
   // var price=form.get('price').value;
   // var description=form.get('description').value;
    return this.http.post(AUTH_API + 'setshippingdate', {
username,password,userid
    }, httpOptions);
  }
}

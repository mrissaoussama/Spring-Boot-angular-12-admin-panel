import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FormGroup } from '@angular/forms';
import { User } from '../models/user';

const AUTH_API = 'http://localhost:8080/api/auth/';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};
const HttpUploadOptions = {
  headers: new HttpHeaders({ })
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  deleteuser(user: User) {
    var username="mod";
    var password="123456";
    var id=user.id;
    return this.http.post(AUTH_API + 'getallusers',{username,password,id});
    }
    confirmUserAccount(token:string): Observable<any>
    {let headers = new Headers();
      headers.append('Content-Type', 'application/json');
      let params = new HttpParams().set("token",token);
      return this.http.get<string>(AUTH_API + 'confirmuseraccount' ,{params: params});

    }
  getallusers() : Observable<any> {
    var username="mod";
    var password="123456"
    return this.http.post(AUTH_API + 'getallusers',{username,password});
    }

  constructor(private http: HttpClient) { }

  update(id: any, username,form: FormGroup) : Observable<any> {


    var password=form.get('password').value;
    var name=form.get('name').value;
    var age=form.get('age').value;
    var surname=form.get('surname').value;
    var address=form.get('address').value;
   var city=form.get('city').value;
    var country=form.get('country').value;
var job=form.get('job').value;
 var description=form.get('description').value;

    //  var username="mod";
    //  var password="123456";
    //  var email="mrissaoussama@gmail.com";




    return this.http.post(AUTH_API + 'user-profile', {id,
     username,password
     ,name
     ,age,surname,address,city,country,job,description

    }, httpOptions);  }

    updateprofileimage(form: FormData) : Observable<any> {


      return this.http.post(AUTH_API + 'updateprofileimage', form);  }

  login(username: string, password: string): Observable<any> {
    return this.http.post(AUTH_API + 'login', {
      username,
      password
    }, httpOptions);
  }

  register(username: string, email: string, password: string): Observable<any> {
    return this.http.post(AUTH_API + 'register', {
      username,
      email,
      password
    }, httpOptions);
  }
}

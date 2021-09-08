import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from './_services/token-storage.service';
import { Router } from "@angular/router";
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  private roles: string[] = [];
  isLoggedIn = false;
  showAdminBoard = false;
  showModeratorBoard = false;
  username?: string;
  password:string;
  email: string;
  name: string;
  age: number;
  surname: string;
  address: string;
  city:string;
  country:string;
  job: string;
  description:string;
  constructor(private tokenStorageService: TokenStorageService,private router: Router) { }

  ngOnInit(): void {
    this.isLoggedIn = !!this.tokenStorageService.getToken();

    if (this.isLoggedIn) {
      const user = this.tokenStorageService.getUser();
      this.roles = user.roles;
this.email=user.email;
this.name=user.name;
this.age=user.age;
this.password=user.password
    
      this.username = user.username;
    }
  }

  logout(): void {
    this.tokenStorageService.signOut();
    this.router.navigate(['/login']);
  }
}
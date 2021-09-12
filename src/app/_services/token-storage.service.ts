import { Role } from './../models/role.model';
import { Injectable } from '@angular/core';
import { User } from '../models/user';
import { Router } from '@angular/router';

const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {
  isAdmin(): boolean {
    let x = this.getUser().roles.filter(item => item.name.includes("ROLE_ADMIN")).length > 0;
    return x;
  }
  constructor(private router: Router) { }

  signOut(): void {
    window.sessionStorage.clear();
    this.router.navigate(['/login']);

  }

  public saveToken(token: string): void {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);
  }

  public getToken(): string | null {
    return window.sessionStorage.getItem(TOKEN_KEY);
  }
  public saveUser(user: User): void {
    window.sessionStorage.removeItem(USER_KEY);
    (user);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }
  public getUsername(): string {
    return this.getUser().username;
  }
  public getPassword(): string {
    return this.getUser().password;
  }
  public getRoles(): Role[] {
    return this.getUser().roles;
  }
  public getUserId(): number {
    return this.getUser().id;
  }
  public getUser(): User {
    const user = window.sessionStorage.getItem(USER_KEY);
    if (user) {
      return JSON.parse(user);
    }
    else return null;
  }
}

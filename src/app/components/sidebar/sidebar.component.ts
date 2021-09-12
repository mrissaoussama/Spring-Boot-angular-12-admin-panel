import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginComponent } from 'src/app/pages/login/login.component';
import { AuthService } from 'src/app/_services/auth.service';
import { TokenStorageService } from 'src/app/_services/token-storage.service';

declare interface RouteInfo {
  path: string;
  title: string;
  icon: string;
  class: string;
}
export var adminROUTES: RouteInfo[] = [
  // { path: '/dashboard', title: 'Dashboard',  icon: 'ni-tv-2 text-primary', class: '' },
  //{ path: '/icons', title: 'Icons',  icon:'ni-planet text-blue', class: '' },
  // { path: '/maps', title: 'Maps',  icon:'ni-pin-3 text-orange', class: '' },
  { path: '/products', title: 'Products', icon: 'ni-bullet-list-67 text-red', class: '' },
  { path: '/users', title: 'Users', icon: 'ni-bullet-list-67 text-red', class: '' },
  { path: '/shopping-carts', title: 'Shopping Carts', icon: 'ni-bullet-list-67 text-red', class: '' },

];
export var ROUTES: RouteInfo[] = [
  // { path: '/dashboard', title: 'Dashboard',  icon: 'ni-tv-2 text-primary', class: '' },
  //{ path: '/icons', title: 'Icons',  icon:'ni-planet text-blue', class: '' },
  // { path: '/maps', title: 'Maps',  icon:'ni-pin-3 text-orange', class: '' },


];

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

  public menuItems: any[];
  public isCollapsed = true;
  public islogged: boolean;
  constructor(private router: Router, private authService: AuthService, private tokenStorage: TokenStorageService) { }

  ngOnInit() {
    this.islogged = this.tokenStorage.getUser() != null;
    if (this.islogged)
      if (this.tokenStorage.isAdmin())
        this.menuItems = adminROUTES.filter(menuItem => menuItem);
      else
        this.menuItems = ROUTES.filter(menuItem => menuItem);

    else (this.logout())
    this.router.events.subscribe((event) => {
      this.isCollapsed = true;

    });
  }
  logout(): void {
    this.islogged = false;
    this.tokenStorage.signOut();
      window.location.reload;

    this.router.navigate(['/login']);
  }
}

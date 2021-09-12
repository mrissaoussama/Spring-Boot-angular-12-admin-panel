import { Routes } from '@angular/router';

import { DashboardComponent } from '../../pages/dashboard/dashboard.component';
import { UserProfileComponent } from '../../pages/user-profile/user-profile.component';
import { ProductsComponent } from '../../pages/products/products.component';
import { UsersComponent } from 'src/app/pages/users/users.component';
import { ShoppingCartsComponent } from 'src/app/pages/shopping-carts/shopping-carts.component';
import { InvoiceComponent } from 'src/app/modal/invoice/invoice.component';
export const AdminLayoutRoutes: Routes = [
  { path: 'dashboard', component: DashboardComponent },
  { path: 'user-profile', component: UserProfileComponent },
  { path: 'products', component: ProductsComponent },
  { path: 'users', component: UsersComponent },
  { path: 'shopping-carts', component: ShoppingCartsComponent },

];

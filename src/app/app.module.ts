import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { AuthLayoutComponent } from './layouts/auth-layout/auth-layout.component';
import { UserProfileComponent } from './pages/user-profile/user-profile.component';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import {MatDialogModule} from '@angular/material/dialog';
import { BrowserModule } from '@angular/platform-browser';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { MatInputModule} from "@angular/material/input";
import {MatPaginatorModule } from "@angular/material/paginator";
import { MatTableModule } from "@angular/material/table";
import {MatSortModule } from "@angular/material/sort";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { AppRoutingModule } from './app.routing';
import { ComponentsModule } from './components/components.module';
import { ProductformComponent } from './modal/productform/productform.component';
import { MatSelectModule } from '@angular/material/select';
import { CategoryformComponent } from './modal/categoryform/categoryform.component';
import { MessageboxComponent } from './modal/messagebox/messagebox.component';
import { UsersComponent } from './pages/users/users.component';
import { ShoppingCartsComponent } from './pages/shopping-carts/shopping-carts.component';
import { InvoiceComponent } from './modal/invoice/invoice.component';

@NgModule({
  imports: [
    BrowserAnimationsModule,
    BrowserModule,
    FormsModule,
    HttpClientModule,
    ComponentsModule,
    NgbModule,
    RouterModule,
    AppRoutingModule,
    MatSelectModule ,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatProgressSpinnerModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatDialogModule,
    ReactiveFormsModule,
  ],
  declarations: [
    AppComponent,
    AdminLayoutComponent,
    AuthLayoutComponent,
    ProductformComponent,
    CategoryformComponent,
    MessageboxComponent,
    UsersComponent,
    ShoppingCartsComponent,
    InvoiceComponent
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

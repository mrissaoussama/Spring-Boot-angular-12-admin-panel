import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ClipboardModule } from 'ngx-clipboard';
import { MatInputModule} from "@angular/material/input";
  import {MatPaginatorModule } from "@angular/material/paginator";
  import { MatTableModule } from "@angular/material/table";
  import {
    MatSortModule } from "@angular/material/sort";
  import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { AdminLayoutRoutes } from './admin-layout.routing';
import { DashboardComponent } from '../../pages/dashboard/dashboard.component';
import { ProductsComponent } from '../../pages/products/products.component';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import {MatDialogModule} from '@angular/material/dialog';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { UserProfileComponent } from 'src/app/pages/user-profile/user-profile.component';

// import { ToastrModule } from 'ngx-toastr';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(AdminLayoutRoutes),
    FormsModule,
    HttpClientModule,
    NgbModule,ReactiveFormsModule,
    ClipboardModule,
    MatInputModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,

    MatProgressSpinnerModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,

    MatDialogModule
  ],
  declarations: [
    DashboardComponent,
    UserProfileComponent,
    ProductsComponent,


  ]
})

export class AdminLayoutModule {}

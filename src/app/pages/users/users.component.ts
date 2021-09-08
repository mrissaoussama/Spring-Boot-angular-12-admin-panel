import { AuthService } from './../../_services/auth.service';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ProductService } from 'src/app/_services/product.service';
import { TokenStorageService } from 'src/app/_services/token-storage.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ProductformComponent } from 'src/app/modal/productform/productform.component';
import { MatPaginator } from '@angular/material/paginator';
import { CategoryformComponent } from 'src/app/modal/categoryform/categoryform.component';
import { User } from 'src/app/models/user';
@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  columnsToDisplay = ["id","username","fullname","email","status","roles","action"];
  users:User[]
  dataSource: MatTableDataSource<User> = null;

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private token: TokenStorageService,private authService:AuthService) { }

  ngOnInit() {
    this.getallusers()
  }
  getallusers() {
    this.authService.getallusers().subscribe(
      data => {
        console.log(data)
        this.users = data.users;
        console.log(this.users);
        this.dataSource = new MatTableDataSource(this.users);
        setTimeout(() => {
          this.dataSource.sort = this.sort;
          this.dataSource.paginator = this.paginator;
        });
      },
      err => {
        (err);
      }
    );
  }
  public doFilter = (value: string,type:String) => {
    switch (type)
    {
    case 'user':
    this.dataSource.filter = value.trim().toLocaleLowerCase();

    }
  }
  deleteuser(user:User): any {
    console.log(user)
    this.authService.deleteuser(user).subscribe(
      data => {
        console.log(data)
        this.getallusers()
      },
      err => {
        console.log(err);
      }
    );
  }

}

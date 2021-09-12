import { Category } from './../../models/category.model';
import { ProductService } from './../../_services/product.service';
import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Product } from 'src/app/models/product.model';
import { MatDialog } from '@angular/material/dialog';
import { MatSelect } from '@angular/material/select';
@Component({
  selector: 'app-productform',
  templateUrl: './productform.component.html',
  styleUrls: ['./productform.component.css']
})
export class ProductformComponent implements OnInit {
  categories: Category[];
  errors: String;
  constructor(
    public dialogRef: MatDialogRef<ProductformComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Product, private productservice: ProductService) { }
  getAllCategories(): any {
    this.productservice.getAllCategories().subscribe(
      data => {
        this.categories = data.categories;

      },
      err => {
        (err);
      }
    );
  }
  onNoClick(): void {
    this.dialogRef.close();
  }
  validate(data: Product): boolean {
    // data = data.price.replace(/,/g, '.')
    this.errors = "";
    if (this.data.price <= 0)
      this.errors += "price must not be zero or negative. "
    if (this.data.name == null || this.data.name == "")
      this.errors += "name must not be empty\n"
    return (this.errors == "")
  }
  save(data: any): any {
    return this.dialogRef.afterClosed()
  }

  ngOnInit() {
    this.getAllCategories();


  }

}

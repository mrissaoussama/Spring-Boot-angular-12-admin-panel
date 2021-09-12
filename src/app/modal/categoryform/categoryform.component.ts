import { Category } from './../../models/category.model';
import { ProductService } from './../../_services/product.service';
import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Product } from 'src/app/models/product.model';
import { MatDialog } from '@angular/material/dialog';
import { MatSelect } from '@angular/material/select';
@Component({
  selector: 'app-categoryform',
  templateUrl: './categoryform.component.html',
  styleUrls: ['./categoryform.component.css']
})
export class CategoryformComponent implements OnInit {

  categories: Category[];
  errors: String = "";
  constructor(
    public dialogRef: MatDialogRef<CategoryformComponent>,
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
  validate(data: any): boolean {
    this.errors = ""

    if (this.data.name == null || this.data.name == "")
      this.errors += "name must not be empty\n"
    return (this.errors == "")
  }
  save(data: any): any {
    return this.dialogRef.afterClosed()
  }

  ngOnInit() {
    this.getAllCategories();
    if (this.data.name == "")
      this.data.name = ""


  }

}

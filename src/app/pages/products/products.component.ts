import { ProductRequest } from './../../models/productrequest.model';
import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { CategoryformComponent } from 'src/app/modal/categoryform/categoryform.component';
import { MessageboxComponent } from 'src/app/modal/messagebox/messagebox.component';
import { ProductformComponent } from 'src/app/modal/productform/productform.component';
import { ProductService } from 'src/app/_services/product.service';
import { TokenStorageService } from 'src/app/_services/token-storage.service';
import { Category } from './../../models/category.model';
import { Product } from './../../models/product.model';
@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit, AfterViewInit {
  columnsToDisplay = ["id", 'name', "price", "description", "category", "images", "action"];
  categorycolumnsToDisplay = ["id", 'name', "action"];

  dataSource: MatTableDataSource<Product> = null;
  categoriesdatasource: MatTableDataSource<Category> = null;
  products: Product[] = [];
  categories: Category[] = [];
  productslength = 0;
  constructor(public productservice: ProductService, private token: TokenStorageService
    , public dialog: MatDialog
    // ,@Inject(DOCUMENT) document:Document
  ) { }
  ngAfterViewInit(): void {
  }
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild('productsearch') productsearch: ElementRef;
  counter(i: number) {
    return new Array(i);
  }
  loadproductresults(): void {
    this.paginator.page.subscribe(() => {
      const productrequest = new ProductRequest(
        "mod", "123456", 0, this.productsearch.nativeElement.value,
        this.productsearch.nativeElement.value, 0, null, false, 'name', 'asc', this.paginator.pageSize, this.paginator.getNumberOfPages())
      this.productservice.findProduct(productrequest).subscribe(
        data => {
          this.products = data.list;
          (this.products);
          this.productslength = data.totalitems;
          this.dataSource = new MatTableDataSource(this.products);
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
    )
  }
  ngOnInit() {
    //  this.productsearch.nativeElement=""
    this.refreshproduct()
    this.getAllCategories()
  }

  refreshproduct() {
    this.productservice.getAllProducts().subscribe(
      data => {
        this.products = data.list;
        (this.products);
        this.productslength = data.totalitems;
        this.dataSource = new MatTableDataSource(this.products);
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
  public doFilter = (value: string, type: String) => {
    switch (type) {
      case 'product':
        this.dataSource.filter = value.trim().toLocaleLowerCase(); break;
      case 'category':
        this.categoriesdatasource.filter = value.trim().toLocaleLowerCase();

    }
  }
  openDialog(product?: Product): void {
    if (product === undefined)
      product = new Product(0, "", "", 0, this.categories[0], false)
    const dialogRef = this.dialog.open(ProductformComponent, {
      width: '350px',
      data: {
        id: product.id, name: product.name, description: product.description,
        price: product.price, category: product.category
        , images: product.images
      }
    });
    dialogRef.afterClosed().subscribe(res => {
      if (JSON.stringify(product) != JSON.stringify(res)) {
        if (product.id != res.id)
          console.log("error")
        else {
          this.updateProduct(res)
          this.refreshproduct()
        }
      }



    });
  }
  opencategorydialog(category?: Category): void {
    if (category === undefined || category.name == null)
      category = new Category(0, "");
    const dialogRef = this.dialog.open(CategoryformComponent, {
      width: '350px',
      data: {
        id: category.id, name: category.name
      }
    });
    dialogRef.afterClosed().subscribe(res => {
      if (res === undefined)
        return;
      if (JSON.stringify(category) == JSON.stringify(res))
        return;
      else {
        if (category.id != res.id)
          return;
        else {
          if (category.id == 0)
            this.addCategory(res);
          else
            this.updateCategory(res);
          this.getAllCategories();
        }
      }
    });
  }
  @ViewChild('produtimage', { static: false })
  produtimage: HTMLImageElement;

  changeproductimage() {
    this.produtimage.src =
      '/assets/Productimages/default.jpg';
  }

  public onFileChanged(event, id, i) {
    this.updateProductImage(event, id, i);

  }
  public fileExists(id, number): boolean {
    var exists = false;
    this.productservice.fileExists(id, number).subscribe(
      data => { exists = data }
    )
    return exists;
  }
  updateProductImage(event, id, i): void {
    const image: FormData = new FormData();
    image.append('image', event.target.files[0]);
    image.append('username', "mod");
    image.append('password', "123456");
    image.append('id', id);
    image.append('number', i);
    this.productservice.updateProductImage(image).subscribe(
      data => {
        window.location.reload()
      },
      err => {
        this.messagebox("cannot update image");
      }
    );

  }
  updateProduct(product: Product): any {
    this.productservice.updateProduct(product).subscribe(
      data => {
        var objIndex = this.products.findIndex((obj => obj.id == product.id));
        this.products[objIndex] = product
        this.dataSource = new MatTableDataSource(this.products)
        this.messagebox(data.message);


      },
      err => {
        this.messagebox("error updating product");

      }
    );
  }

  deleteProduct(product: Product): any {
    this.productservice.deleteProduct(product).subscribe(
      data => {
        this.messagebox(data.message);
        this.refreshproduct()
      },
      err => {
        this.messagebox("Error deleting product");
      }
    );
  }
  deleteCategory(category: Category): any {
    this.productservice.deleteCategory(category).subscribe(
      data => {
        this.messagebox(data.message);
        this.getAllCategories()
      },
      err => {
        this.messagebox("Error deleting category, please make sure no products are in this category.");
      }
    );
  }
  addCategory(category: Category): any {
    this.productservice.addCategory(category).subscribe(
      data => {
        this.messagebox(data.message);
        this.getAllCategories()
      },
      err => {
        this.messagebox("Error adding category. make sure it does not already exist");
      }
    );
  }
  updateCategory(category: Category): any {
    this.productservice.updateCategory(category).subscribe(
      data => {
        var objIndex = this.categories.findIndex((obj => obj.id == category.id));
        this.categories[objIndex] = category
        this.categoriesdatasource = new MatTableDataSource(this.categories)

        this.messagebox(data.message);
      },
      err => {
        this.messagebox(err.message);

      }
    );
  }
  getAllCategories(): any {
    this.productservice.getAllCategories().subscribe(
      data => {
        this.categories = data.categories;
        this.categoriesdatasource = new MatTableDataSource(this.categories);
        setTimeout(() => {
          this.categoriesdatasource.sort = this.sort;
          this.categoriesdatasource.paginator = this.paginator;
        });

      },
      err => {
        this.messagebox("error getting categories");
      }
    );
  }
  messagebox(body: string, title?: string) {
    if (title === undefined)
      title = "Notice"
    const dialogRef = this.dialog.open(MessageboxComponent, {
      width: '350px',
      data: {
        title: title, body: body
      }
    });
  }

}



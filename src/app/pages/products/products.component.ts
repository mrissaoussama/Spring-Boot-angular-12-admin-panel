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
  categorycolumnsToDisplay = ["id", 'name',"action"];

  dataSource: MatTableDataSource<Product> = null;
  categoriesdatasource:MatTableDataSource<Category>=null;
  products: Product[]=[];
  categories:Category[]=[];
  productslength=0;
  constructor(public productservice: ProductService, private token: TokenStorageService
    , public dialog: MatDialog
   // ,@Inject(DOCUMENT) document:Document
    ) { }
  ngAfterViewInit(): void {
  }
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild('productsearch') productsearch:ElementRef;
  counter(i: number) {
    return new Array(i);
  }
  loadproductresults(): void {
    console.log("test")

    this.paginator.page.subscribe(()=>{
       const productrequest=new ProductRequest(
        "mod","123456",0,this.productsearch.nativeElement.value,
        this.productsearch.nativeElement.value,0,null,false,'name','asc',this.paginator.pageSize,this.paginator.getNumberOfPages())
      this.productservice.findproduct(productrequest).subscribe(
      data => {
        this.products = data.list;
        console.log(this.products);
        this.productslength=data.totalitems;
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
    this.getallcategories()
  }

  refreshproduct() {
    this.productservice.getallproducts().subscribe(
      data => {
        this.products = data.list;
        console.log(this.products);
        console.log(data);
        this.productslength=data.totalitems;
        console.log(this.productslength)
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
  public doFilter = (value: string,type:String) => {
    switch (type)
    {
    case 'product':
    this.dataSource.filter = value.trim().toLocaleLowerCase();
    case 'category':
      this.categoriesdatasource.filter = value.trim().toLocaleLowerCase();

    }
  }
  openDialog(product?: Product): void {
    if (product===undefined)
    product=new Product(0,"","",0,this.categories[0],false)
    const dialogRef = this.dialog.open(ProductformComponent, {
      width: '350px',
      data: {
        id: product.id, name: product.name, description: product.description,
        price: product.price, category: product.category
        , images: product.images
      }
    });
    dialogRef.afterClosed().subscribe(res => {
      if (JSON.stringify(product) == JSON.stringify(res))
        console.log("same result")
      else {
        if (product.id != res.id)
          console.log("error")
        else {
          this.updateproduct(res)
          this.refreshproduct()
        }
      }



    });
  }
  opencategorydialog(category?: Category): void {
if(category===undefined||category.name==null)
category=new Category(0,"");
    const dialogRef = this.dialog.open(CategoryformComponent, {
      width: '350px',
      data: {
        id: category.id, name: category.name
      }
    });
    dialogRef.afterClosed().subscribe(res => {
      if (res===undefined)
      return;
      if (JSON.stringify(category) == JSON.stringify(res))
        return;
      else {
        if (category.id != res.id)
          return;
        else {
          if (category.id==0)
          this.addcategory(res);
          else
          this.updatecategory(res);
          this.getallcategories();
          console.log(res)
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
    this.updateproductimage(event, id, i);

  }
  public fileexists(id, number): boolean {
    var exists = false;
    this.productservice.fileexists(id, number).subscribe(
      data => { exists = data }
    )
    return exists;
  }
  updateproductimage(event, id, i): void {
    const image: FormData = new FormData();
    image.append('image', event.target.files[0]);
    image.append('username', "mod");
    image.append('password', "123456");
    image.append('id', id);
    image.append('number', i);
    this.productservice.updateproductimage(image).subscribe(
      data => {
window.location.reload()
 },
      err => {
        this.messagebox("cannot update image");
      }
    );

  }
  updateproduct(product: Product): any {
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

  deleteproduct(product: Product): any {
    console.log(product)
    this.productservice.deleteproduct(product).subscribe(
      data => {
        this.messagebox(data.message);
        this.refreshproduct()
      },
      err => {
        this.messagebox("Error deleting product");
      }
    );
  }
  deletecategory(category: Category): any {
    this.productservice.deletecategory(category).subscribe(
      data => {
        this.messagebox(data.message);
        this.getallcategories()
      },
      err => {
        this.messagebox("Error deleting category, please make sure no products are in this category.");
      }
    );
  }
  addcategory(category: Category): any {
    this.productservice.addcategory(category).subscribe(
      data => {
        this.messagebox(data.message);
this.getallcategories()
      },
      err => {
        this.messagebox("Error adding category. make sure it does not already exist");
      }
    );
  }
  updatecategory(category: Category): any {
    this.productservice.updatecategory(category).subscribe(
      data => {
        var objIndex = this.categories.findIndex((obj => obj.id == category.id));
        this.categories[objIndex] = category
        this.categoriesdatasource = new MatTableDataSource(this.categories)
        console.log(objIndex)
        console.log(category)
        this.messagebox(data.message);
      },
      err => {
        this.messagebox(err.message);

      }
    );
  }
  getallcategories():any
  {
    this.productservice.getallcategories().subscribe(
      data => {
  this.categories=data.categories;
  this.categoriesdatasource = new MatTableDataSource(this.categories);
  setTimeout(() => {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  });

  },
      err => {
        this.messagebox("error getting categories");
      }
    );
  }
messagebox(body:string,title?:string)
{if (title===undefined)
  title="Notice"
  const dialogRef = this.dialog.open(MessageboxComponent, {
    width: '350px',
    data: {
      title: title,  body:body
    }
  });
}

}



import { Category } from './category.model';
import { Product } from './product.model';
export class ProductRequest {
    constructor(
      public username:string,
      public password:string,
        public id: number,
        public name:string,
        public description:string,
        public price:number,
        public category:Category,
        public images:boolean,
        private  sort:string="id",
        private  sortdirection:string="asc",
      private  pagesize:number=10,
      private pagenumber:number=0
      ) {  }
     convert(
         username:string,
         password:string,
           product: Product,
        ) {
          this.username=username;
          this.password=password;
          this.id=product.id
          this.name=product.name
          this.description=product.description
          this.price=product.price
          this.category=product.category
          this.images=product.images
          }
}

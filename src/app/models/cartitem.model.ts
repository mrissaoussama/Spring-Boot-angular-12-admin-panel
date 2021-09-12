import { Shoppingcart } from './shoppingcart.model';
import { Product } from 'src/app/models/product.model';
export class Cartitem {
  constructor(
    public id: number,
    public product: Product,
    public shoppingcart: Shoppingcart,
    public quantity: number,
  ) { }
}

import { Cartitem } from './cartitem.model';
import { User } from 'src/app/models/user';
export class Shoppingcart {
  constructor(
    public id: number,
    public user: User,
    public cartItems: Cartitem[],
  ) { }
}

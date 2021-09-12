import { Cartitem } from './cartitem.model';
export class Cartrequest {
  constructor(
    public username: string,
    public password: string,
    public userid: number,
    public cartItems: Cartitem[]

  ) { }
}

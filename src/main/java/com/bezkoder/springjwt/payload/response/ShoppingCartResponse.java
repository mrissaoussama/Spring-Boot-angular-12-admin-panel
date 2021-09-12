package com.bezkoder.springjwt.payload.response;

import java.util.List;

import com.bezkoder.springjwt.models.ShoppingCart;

import lombok.Data;

@Data
public class ShoppingCartResponse {
  private ShoppingCart shoppingcart;

  private List<ShoppingCart> list;

  public ShoppingCartResponse(ShoppingCart shoppingcart) {
    this.shoppingcart = shoppingcart;
  }

  public ShoppingCartResponse(List<ShoppingCart> list) {
    this.list = list;
  }

}

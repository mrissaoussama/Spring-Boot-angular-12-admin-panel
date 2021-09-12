package com.bezkoder.springjwt.payload.request;

import java.util.Date;

import lombok.Data;

@Data
public class ShoppingCartRequest {
  private String username;
  private String password;
  private Long userid;
  private Date shippingdate = new Date();
  private Date completeddate = new Date();

}

package com.bezkoder.springjwt.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class CartRequest {
  private String username;
  private String password;
  private Long userid;
  private long productid;
  private int quantity;
}

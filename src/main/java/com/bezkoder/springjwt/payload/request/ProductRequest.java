package com.bezkoder.springjwt.payload.request;

import com.bezkoder.springjwt.models.Category;

import lombok.Data;

@Data
public class ProductRequest {
  private String username;
  private String password;
  private Long id;
  private Category category;
  private String name;
  private String description;
  private Float price;
  private boolean images;
  private String sort = "id";
  private String sortdirection = "asc";
  private int pagesize = 10;
  private int pagenumber = 0;
}

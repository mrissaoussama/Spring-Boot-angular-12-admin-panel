package com.bezkoder.springjwt.payload.response;

import java.util.List;

import com.bezkoder.springjwt.models.Category;
import com.bezkoder.springjwt.models.Product;

import org.springframework.data.domain.Page;

import lombok.Data;

@Data
public class ProductResponse {
  private String token;
  private String type = "Bearer";
  private Long id;
  private Category category;
  private String name;
  private String description;
  private Float price;
  private boolean images;
  private List<Product> list;
  private int currentpage;
  private long totalitems;
  private int totalpages;

  public ProductResponse(String token, Long id, Category category, String name, String description, Float price,
      boolean images) {
    this.token = token;
    this.id = id;
    this.category = category;
    this.name = name;
    this.description = description;
    this.price = price;
    this.images = images;
  }

  public ProductResponse(String token, List<Product> list) {
    this.token = token;
    this.list = list;
  }

  public ProductResponse(List<Product> list) {
    this.list = list;
  }

  public ProductResponse(Page<Product> list) {
    this.list = list.getContent();
    this.currentpage = list.getNumber();
    this.totalitems = list.getTotalElements();
    this.totalpages = list.getTotalPages();
  }

}

package com.bezkoder.springjwt.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor
@Entity

public class CartItem{
  @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
@ManyToOne
   private Product product;
   @ManyToOne
   @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
   @JsonIdentityReference(alwaysAsId=true)
      private ShoppingCart shoppingcart;
   private int quantity;
   public CartItem(Product product,int quantity)
   {
     this.product=product;
     this.quantity=quantity;
   }
}

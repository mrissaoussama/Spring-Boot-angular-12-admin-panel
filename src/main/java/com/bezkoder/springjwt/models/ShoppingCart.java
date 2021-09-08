package com.bezkoder.springjwt.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class ShoppingCart {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
    @OneToOne(mappedBy = "shoppingcart")

  private User user;
  @OneToMany(mappedBy = "shoppingcart")
  private Set<CartItem> cartItems;
  private Date createddate;
  private Date shippingdate;
  private Date completeddate;

  public ShoppingCart(User user) {
    this.user = user;
    this.createddate = new Date();
    this.cartItems=new HashSet<>();
  }

public void addproduct( CartItem cartitem)
{boolean exists=false;
if(!this.cartItems.isEmpty())
  for (CartItem item : this.cartItems)
  {
if(item.getProduct().getId().equals(cartitem.getProduct().getId()))
  {
  item.setQuantity(item.getQuantity()+cartitem.getQuantity());
  exists=true;
  break;
  }}
if (exists==false)
    {
      this.cartItems.add(cartitem);
    }
 }
    }


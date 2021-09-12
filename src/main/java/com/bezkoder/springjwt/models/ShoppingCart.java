package com.bezkoder.springjwt.models;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
  @OnDelete(action = OnDeleteAction.CASCADE)

  private User user;
  @OneToMany(mappedBy = "shoppingcart")
  private Set<CartItem> cartItems;
  private Date createddate;
  private Date shippingdate;
  private Date completeddate;

  public ShoppingCart(User user) {
    this.user = user;
    this.createddate = new Date();
    this.cartItems = new HashSet<>();
  }

  public void addProduct(CartItem cartitem) {
    boolean exists = false;
    if (!this.cartItems.isEmpty())
      for (CartItem item : this.cartItems) {
        if (item.getProduct().getId().equals(cartitem.getProduct().getId())) {
          item.setQuantity(item.getQuantity() + cartitem.getQuantity());
          if (item.getQuantity() <= 0)
            removeProduct(item);
          exists = true;
          break;
        }
      }
    if (exists == false) {
      this.cartItems.add(cartitem);
    }
  }

  public void removeProduct(CartItem cartitem) {
    if (!this.cartItems.isEmpty())
      this.cartItems.remove(cartitem);
  }

  public void updateProduct(List<CartItem> cartitems) {
    if (!this.cartItems.isEmpty())
      for (CartItem itemtoupdate : cartitems) {
        for (CartItem item : this.cartItems) {
          if (item.getProduct().getId().equals(itemtoupdate.getProduct().getId())) {
            item.setQuantity(itemtoupdate.getQuantity());
          }
        }
      }
  }

}

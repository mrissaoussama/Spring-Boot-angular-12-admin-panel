package com.bezkoder.springjwt.repository;

import java.util.List;
import java.util.Optional;

import com.bezkoder.springjwt.models.CartItem;
import com.bezkoder.springjwt.models.Product;
import com.bezkoder.springjwt.models.ShoppingCart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  Optional<CartItem> findById(long id);
  List<CartItem>  findByProduct(long id);
  List<CartItem> findByShoppingcart(ShoppingCart shoppingcart);
  Optional<CartItem> findByShoppingcartAndProduct(ShoppingCart shoppingcart,Product product );

}

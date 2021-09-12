package com.bezkoder.springjwt.repository;

import java.util.List;
import java.util.Optional;

import com.bezkoder.springjwt.models.ShoppingCart;
import com.bezkoder.springjwt.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
  Optional<ShoppingCart> findById(long id);

  List<ShoppingCart> findByCartItemsIsNotEmpty();

  Optional<ShoppingCart> findByUser(User user);

}

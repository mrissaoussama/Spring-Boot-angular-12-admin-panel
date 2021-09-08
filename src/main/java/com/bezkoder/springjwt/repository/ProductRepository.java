package com.bezkoder.springjwt.repository;

import java.util.List;
import java.util.Optional;

import com.bezkoder.springjwt.models.Category;
import com.bezkoder.springjwt.models.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	Optional<Product> findByName(String name);
	List<Product> findByNameContaining(String name);
  Page<Product> findByNameContaining(String name,Pageable pageable);
	List<Product> findAllByPrice(double price);
  Page<Product> findAllByPrice(double price,Pageable pageable);
	List<Product> findByNameContainingAndCategory( String name, Category Category);
  Page<Product> findByNameContainingAndCategory( String name, Category Category,Pageable pageable);

    List<Product> findByNameAndCategory( String name, Category Category);

	void delete(Product product);

	List<Product> findByCategory(Category Category);

}

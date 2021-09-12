package com.bezkoder.springjwt.security.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.bezkoder.springjwt.models.CartItem;
import com.bezkoder.springjwt.models.Category;
import com.bezkoder.springjwt.models.Product;
import com.bezkoder.springjwt.models.ShoppingCart;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.CartItemRequest;
import com.bezkoder.springjwt.payload.request.CartRequest;
import com.bezkoder.springjwt.payload.request.CategoryRequest;
import com.bezkoder.springjwt.payload.request.ProductRequest;
import com.bezkoder.springjwt.payload.request.ShoppingCartRequest;
import com.bezkoder.springjwt.payload.response.CategoryResponse;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.payload.response.ProductResponse;
import com.bezkoder.springjwt.payload.response.ShoppingCartResponse;
import com.bezkoder.springjwt.repository.CartItemRepository;
import com.bezkoder.springjwt.repository.CategoryRepository;
import com.bezkoder.springjwt.repository.ProductRepository;
import com.bezkoder.springjwt.repository.ShoppingCartRepository;
import com.bezkoder.springjwt.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductService {
  @Autowired
  ProductRepository productRepository;
  @Autowired
  CategoryRepository categoryRepository;
  @Autowired
  UserRepository userRepository;
  @Autowired
  ShoppingCartRepository shoppingcartRepository;
  @Autowired
  CartItemRepository cartItemRepository;
  @Autowired
  UserDetailsServiceImpl userDetailsServiceImpl;

  public ResponseEntity<?> findProduct(ProductRequest productRequest) {
    if (productRequest.getCategory() != null && productRequest.getName() != null) {
      return findByNameContainingAndCategory(productRequest);
    } else {
      if (productRequest.getName() != null) {
        return findByNameContaining(productRequest);
      } else {
        return findAllProducts(productRequest);
      }
    }
  }

  public Pageable checkPaging(ProductRequest productRequest) {
    if (productRequest.getPagesize() > 0 && productRequest.getPagenumber() >= 0) {
      if (productRequest.getSortdirection() == "desc") {
        return PageRequest.of(productRequest.getPagenumber(), productRequest.getPagesize(),
            Sort.by(productRequest.getSort()).descending());
      } else {
        return PageRequest.of(productRequest.getPagenumber(), productRequest.getPagesize(),
            Sort.by(productRequest.getSort()).ascending());
      }
    }
    return null;
  }

  public ResponseEntity<?> findAllProducts(ProductRequest productRequest) {
    Pageable paging = checkPaging(productRequest);
    if (paging == null)
      return ResponseEntity.ok(new ProductResponse(productRepository.findAll()));
    return ResponseEntity.ok(new ProductResponse(productRepository.findAll(paging)));
  }

  public ResponseEntity<?> findByNameContaining(ProductRequest productRequest) {
    Pageable paging = checkPaging(productRequest);
    if (paging == null)
      return ResponseEntity.ok(new ProductResponse(productRepository.findByNameContaining(productRequest.getName())));
    else
      return ResponseEntity
          .ok(new ProductResponse(productRepository.findByNameContaining(productRequest.getName(), paging)));
  }

  public ResponseEntity<?> findByNameContainingAndCategory(ProductRequest productRequest) {
    Pageable paging = checkPaging(productRequest);
    if (paging == null)
      return ResponseEntity.ok(new ProductResponse(
          productRepository.findByNameContainingAndCategory(productRequest.getName(), productRequest.getCategory())));
    else
      return ResponseEntity.ok(new ProductResponse(productRepository
          .findByNameContainingAndCategory(productRequest.getName(), productRequest.getCategory(), paging)));
  }

  public ResponseEntity<?> addProduct(ProductRequest productRequest) {
    userDetailsServiceImpl.checkAdmin(productRequest.getUsername(), productRequest.getPassword());

    Category category = categoryRepository.findById(productRequest.getCategory().getId()).get();
    if (category == null)
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Category does not exist"));
    if (productRequest.getName() == null || productRequest.getName() == "")
      return ResponseEntity.badRequest().body(new MessageResponse("Error: name must not be empty"));
    if (productRequest.getPrice() == null || productRequest.getPrice() <= 0)
      return ResponseEntity.badRequest().body(new MessageResponse("Error: price must not be empty, zero or negative"));
    Product product = new Product(productRequest);
    productRepository.save(product);
    return ResponseEntity.ok(new MessageResponse("product added successfully!"));
  }

  public ResponseEntity<?> updateProduct(ProductRequest productRequest) {
    userDetailsServiceImpl.checkAdmin(productRequest.getUsername(), productRequest.getPassword());

    Category category = categoryRepository.findById(productRequest.getCategory().getId()).get();
    if (category == null)
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Category does not exist"));
    if (productRequest.getName() == null || productRequest.getName() == "")
      return ResponseEntity.badRequest().body(new MessageResponse("Error: name must not be empty"));
    if (productRequest.getPrice() == null || productRequest.getPrice() <= 0)
      return ResponseEntity.badRequest().body(new MessageResponse("Error: price must not be empty, zero or negative"));
    Product product = new Product(productRequest);
    productRepository.save(product);
    return ResponseEntity.ok(new MessageResponse("product updated successfully!"));
  }

  public ResponseEntity<?> deleteProduct(ProductRequest productRequest) {
    userDetailsServiceImpl.checkAdmin(productRequest.getUsername(), productRequest.getPassword());

    productRepository.deleteById(productRequest.getId());
    return ResponseEntity.ok(new MessageResponse("product deleted successfully"));
  }

  public ResponseEntity<?> saveProductImage(long id, MultipartFile image, int number) {

    boolean exists = true;
    if (number < 0 || number > 4)
      return ResponseEntity.badRequest().body(new MessageResponse("Error: product can only have 5 images"));
    Product Product = getProduct(id);
    OutputStream out = null;
    try {
      Files.createDirectories(Paths.get("src/assets/productimages/" + id + "/"));
      File tempFile = new File("src/assets/productimages/" + id + "/" + number + ".jpg");
      exists = tempFile.exists();
      out = new FileOutputStream("src/assets/productimages/" + id + "/" + number + ".jpg");
      out.write(image.getBytes());
      Product.setImages(true);
      productRepository.save(Product);
      if (exists)
        return ResponseEntity.ok(new MessageResponse("product image updated successfully"));
      else
        return ResponseEntity.ok(new MessageResponse("product image added successfully"));
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().body(new MessageResponse("Error: image could not be saved"));
    } finally {
      try {
        out.flush();
        out.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private Product getProduct(long id) {

    Product product = productRepository.findById(id).get();
    checkProduct(product);
    return product;
  }

  private void checkProduct(Product product) {
    if (product == null)
      productDoesNotExist();
  }

  private ResponseEntity<?> productDoesNotExist() {
    return ResponseEntity.badRequest().body(new MessageResponse("Error: product does not exist"));
  }

  public ResponseEntity<?> updateCategory(@Valid CategoryRequest categoryRequest) {
    userDetailsServiceImpl.checkAdmin(categoryRequest.getUsername(), categoryRequest.getPassword());
    Category category = categoryRepository.findById(categoryRequest.getId()).get();
    category.setName(categoryRequest.getName());
    categoryRepository.save(category);
    return ResponseEntity.ok(new MessageResponse("Category updated successfully!"));
  }

  public ResponseEntity<?> deleteCategory(@Valid CategoryRequest categoryRequest) {
    userDetailsServiceImpl.checkAdmin(categoryRequest.getUsername(), categoryRequest.getPassword());
    System.out.print(categoryRequest.getId());
    categoryRepository.deleteById(categoryRequest.getId());
    return ResponseEntity.ok(new MessageResponse("Category deleted successfully!"));
  }

  public ResponseEntity<?> addCategory(@Valid CategoryRequest categoryRequest) {
    userDetailsServiceImpl.checkAdmin(categoryRequest.getUsername(), categoryRequest.getPassword());
    Category category = new Category(categoryRequest.getName());
    categoryRepository.save(category);
    return ResponseEntity.ok(new MessageResponse("Category added successfully!"));
  }

  public ResponseEntity<?> getAllCategories(CategoryRequest categoryRequest) {
    userDetailsServiceImpl.checkAdmin(categoryRequest.getUsername(), categoryRequest.getPassword());

    List<Category> categorylist = categoryRepository.findAll();
    return ResponseEntity.ok(new CategoryResponse(categorylist));
  }

  public ResponseEntity<?> addToCart(CartRequest cartRequest) {
    userDetailsServiceImpl.checkAdminOrConcernedUser(cartRequest.getUsername(), cartRequest.getPassword(),
        cartRequest.getUserid());

    // if (cartRequest.getQuantity() <= 0)
    // return ResponseEntity.badRequest().body(new MessageResponse("Error: quantity
    // must not bigger than 1"));
    if (cartRequest.getProductid() <= 0)
      return ResponseEntity.badRequest().body(new MessageResponse("Error: product not valid"));
    if (cartRequest.getUserid() == null || cartRequest.getUserid() <= 0)
      return ResponseEntity.badRequest().body(new MessageResponse("Error: cart's user is not valid"));
    User user = null;
    Optional<User> ouser = userRepository.findById(cartRequest.getUserid());
    if (ouser.isEmpty()) {
      userDetailsServiceImpl.userNotFound();
    } else
      user = ouser.get();
    ShoppingCart shoppingcart;
    if (user.getShoppingcart() == null) {
      shoppingcart = new ShoppingCart(user);
      shoppingcartRepository.save(shoppingcart);
      user.setShoppingcart(shoppingcart);
      userRepository.save(user);
    }

    Product product = productRepository.findById(cartRequest.getProductid()).get();

    if (product == null)
      return ResponseEntity.badRequest().body(new MessageResponse("Error: product does not exist"));

    CartItem cartitem = null;
    Optional<CartItem> ocartitem = cartItemRepository.findByShoppingcartAndProduct(user.getShoppingcart(), product);
    if (ocartitem.isEmpty()) {
      cartitem = new CartItem(product, cartRequest.getQuantity());
      cartitem.setShoppingcart(user.getShoppingcart());
      cartItemRepository.save(cartitem);
    } else
      cartitem = ocartitem.get();
    shoppingcart = user.getShoppingcart();
    shoppingcart.addProduct(cartitem);
    shoppingcartRepository.save(shoppingcart);
    return ResponseEntity.ok(new MessageResponse("product added to cart"));
  }

  public ResponseEntity<?> removeCartItem(@Valid CartRequest cartRequest) {
    userDetailsServiceImpl.checkAdminOrConcernedUser(cartRequest.getUsername(), cartRequest.getPassword(),
        cartRequest.getUserid());
    User user = userRepository.findByUsernameIgnoreCase(cartRequest.getUsername()).get();
    ShoppingCart shoppingCart = user.getShoppingcart();
    Product product = getProduct(cartRequest.getProductid());
    System.out.println(shoppingCart.getId());
    System.out.println(product.getId());

    CartItem cartItem = cartItemRepository.findByShoppingcartAndProduct(shoppingCart, product).get();
    shoppingCart.removeProduct(cartItem);
    shoppingcartRepository.save(shoppingCart);
    cartItemRepository.delete(cartItem);
    return ResponseEntity.ok(new MessageResponse("Item removed"));

  }

  public ResponseEntity<?> getShoppingcart(CartRequest cartRequest) {
    userDetailsServiceImpl.checkAdminOrConcernedUser(cartRequest.getUsername(), cartRequest.getPassword(),
        cartRequest.getUserid());
    if (cartRequest.getUserid() == null)
      return ResponseEntity.badRequest().body(new MessageResponse("Error: cart's user is not valid"));
    User user = userRepository.findById(cartRequest.getUserid()).get();
    if (user == null)
      return ResponseEntity.badRequest().body(new MessageResponse("Error: user does not exist"));
    ShoppingCart shoppingcart = user.getShoppingcart();
    if (shoppingcart == null)
      return ResponseEntity.badRequest().body(new MessageResponse("Error: user has no cart"));
    if (shoppingcart.getCartItems().isEmpty())
      return ResponseEntity.badRequest().body(new MessageResponse("Error: empty cart"));
    return ResponseEntity.ok(new ShoppingCartResponse(shoppingcart));
  }

  public ResponseEntity<?> getAllShoppingCarts(CartRequest cartRequest) {
    userDetailsServiceImpl.checkAdmin(cartRequest.getUsername(), cartRequest.getPassword());
    List<ShoppingCart> shoppingcart = shoppingcartRepository.findByCartItemsIsNotEmpty();
    if (shoppingcart == null)
      return ResponseEntity.badRequest().body(new MessageResponse("Error: no carts"));
    return ResponseEntity.ok(new ShoppingCartResponse(shoppingcart));
  }

  public ResponseEntity<?> setShippingDate(@Valid ShoppingCartRequest shoppingcartRequest) {
    userDetailsServiceImpl.checkAdmin(shoppingcartRequest.getUsername(), shoppingcartRequest.getPassword());
    if (shoppingcartRequest.getUserid() == null)
      return ResponseEntity.badRequest().body(new MessageResponse("Error: cart's user is not valid"));
    User user = userRepository.findById(shoppingcartRequest.getUserid()).get();
    if (user == null)
      return ResponseEntity.badRequest().body(new MessageResponse("Error: user does not exist"));
    ShoppingCart shoppingcart = shoppingcartRepository.findByUser(user).get();
    if (shoppingcart == null)
      return ResponseEntity.badRequest().body(new MessageResponse("Error: user has no cart"));
    if (shoppingcart.getCartItems().isEmpty())
      return ResponseEntity.badRequest().body(new MessageResponse("Error: empty cart"));
    if (shoppingcart.getShippingdate() != null)
      if (shoppingcartRequest.getShippingdate().before(shoppingcart.getShippingdate()))
        return ResponseEntity.badRequest().body(new MessageResponse("Error: invalid date"));
    if (shoppingcartRequest.getShippingdate() == null)
      return ResponseEntity.badRequest().body(new MessageResponse("Error: invalid date"));
    shoppingcart.setShippingdate(shoppingcartRequest.getShippingdate());
    shoppingcartRepository.save(shoppingcart);
    return ResponseEntity.ok(new ShoppingCartResponse(shoppingcart));
  }

  public ResponseEntity<?> setCompletedDate(@Valid ShoppingCartRequest shoppingcartRequest) {
    userDetailsServiceImpl.checkAdmin(shoppingcartRequest.getUsername(), shoppingcartRequest.getPassword());
    if (shoppingcartRequest.getUserid() == null)
      return ResponseEntity.badRequest().body(new MessageResponse("Error: cart's user is not valid"));
    User user = userRepository.findById(shoppingcartRequest.getUserid()).get();
    if (user == null)
      return ResponseEntity.badRequest().body(new MessageResponse("Error: user does not exist"));
    ShoppingCart shoppingcart = shoppingcartRepository.findByUser(user).get();
    if (shoppingcart == null)
      return ResponseEntity.badRequest().body(new MessageResponse("Error: user has no cart"));
    if (shoppingcart.getCartItems().isEmpty())
      return ResponseEntity.badRequest().body(new MessageResponse("Error: empty cart"));
    if (shoppingcart.getCompleteddate() != null)
      if (shoppingcartRequest.getCompleteddate().before(shoppingcart.getCompleteddate()))
        return ResponseEntity.badRequest().body(new MessageResponse("Error: invalid date"));
    if (shoppingcartRequest.getCompleteddate() == null)
      return ResponseEntity.badRequest().body(new MessageResponse("Error: invalid date"));
    shoppingcart.setCompleteddate(shoppingcartRequest.getCompleteddate());
    shoppingcartRepository.save(shoppingcart);
    return ResponseEntity.ok(new ShoppingCartResponse(shoppingcart));
  }

  public ResponseEntity<?> updateShoppingCartItems(@Valid CartItemRequest cartItemRequest) {
    userDetailsServiceImpl.checkAdminOrConcernedUser(cartItemRequest.getUsername(), cartItemRequest.getPassword(),
        cartItemRequest.getUserid());

    if (cartItemRequest.getCartItems().isEmpty())
      return ResponseEntity.badRequest().body(new MessageResponse("Error: empty cart"));
    User user = userRepository.findById(cartItemRequest.getUserid()).get();

    ShoppingCart shoppingcart = user.getShoppingcart();
    shoppingcart.updateProduct(cartItemRequest.getCartItems());
    shoppingcartRepository.save(shoppingcart);
    return ResponseEntity.ok(new MessageResponse("Cart updated"));

  }

}

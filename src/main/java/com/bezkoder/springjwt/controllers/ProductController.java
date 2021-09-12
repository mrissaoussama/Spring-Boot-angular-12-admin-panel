package com.bezkoder.springjwt.controllers;

import javax.validation.Valid;

import com.bezkoder.springjwt.payload.request.CartItemRequest;
import com.bezkoder.springjwt.payload.request.CartRequest;
import com.bezkoder.springjwt.payload.request.CategoryRequest;
import com.bezkoder.springjwt.payload.request.ProductRequest;
import com.bezkoder.springjwt.payload.request.ShoppingCartRequest;
import com.bezkoder.springjwt.repository.CategoryRepository;
import com.bezkoder.springjwt.repository.ProductRepository;
import com.bezkoder.springjwt.security.jwt.JwtUtils;
import com.bezkoder.springjwt.security.services.ProductService;
import com.bezkoder.springjwt.security.services.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/product")
public class ProductController {
  @Autowired
  AuthenticationManager authenticationManager;
  @Autowired
  ProductRepository productRepository;
  @Autowired
  CategoryRepository categoryRepository;
  @Autowired
  JwtUtils jwtUtils;
  @Autowired
  UserDetailsServiceImpl userDetailsServiceImpl;
  @Autowired
  ProductService productService;

  @RequestMapping(value = "/updateProductimage", consumes = { "multipart/mixed", "multipart/form-data" })
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseBody
  public ResponseEntity<?> saveProductImage(@Valid @RequestPart("image") MultipartFile image,
      @Valid @RequestParam("number") int number, @Valid @RequestParam("id") long id,
      @Valid @RequestPart("username") String username, @Valid @RequestPart("password") String password) {
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(username, password));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return productService.saveProductImage(id, image, number);
  }

  @PostMapping("/findProduct")
  public ResponseEntity<?> findProduct(@Valid @RequestBody ProductRequest productRequest) {
    return productService.findProduct(productRequest);
  }

  @PostMapping("/addProduct")
  public ResponseEntity<?> addProduct(@Valid @RequestBody ProductRequest productRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(productRequest.getUsername(), productRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return productService.addProduct(productRequest);

  }

  @PostMapping("/updateProduct")
  public ResponseEntity<?> updateProduct(@Valid @RequestBody ProductRequest productRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(productRequest.getUsername(), productRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return productService.updateProduct(productRequest);

  }

  @PostMapping("/deleteProduct")
  public ResponseEntity<?> deleteProduct(@Valid @RequestBody ProductRequest productRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(productRequest.getUsername(), productRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return productService.deleteProduct(productRequest);

  }

  @PostMapping("/addCategory")
  public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(categoryRequest.getUsername(), categoryRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return productService.addCategory(categoryRequest);
  }

  @PostMapping("/deleteCategory")
  public ResponseEntity<?> deleteCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
    return productService.deleteCategory(categoryRequest);
  }

  @PostMapping("/updateCategory")
  public ResponseEntity<?> updateCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
    return productService.updateCategory(categoryRequest);
  }

  @PostMapping("/getAllCategories")
  public ResponseEntity<?> getAllCategories(@Valid @RequestBody CategoryRequest categoryRequest) {
    return productService.getAllCategories(categoryRequest);
  }

  // shopping cart
  @PostMapping("/addtocart")
  public ResponseEntity<?> addToCart(@Valid @RequestBody CartRequest cartRequest) {
    return productService.addToCart(cartRequest);
  }

  @PostMapping("/getAllShoppingCarts")
  public ResponseEntity<?> getAllShoppingCarts(@Valid @RequestBody CartRequest cartRequest) {
    return productService.getAllShoppingCarts(cartRequest);
  }

  @PostMapping("/getShoppingCart")
  public ResponseEntity<?> getShoppingcart(@Valid @RequestBody CartRequest cartRequest) {
    return productService.getShoppingcart(cartRequest);
  }

  @PostMapping("/setShippingDate")
  public ResponseEntity<?> setShippingDate(@Valid @RequestBody ShoppingCartRequest shoppingcartRequest) {
    return productService.setShippingDate(shoppingcartRequest);
  }

  @PostMapping("/setCompletedDate")
  public ResponseEntity<?> setCompletedDate(@Valid @RequestBody ShoppingCartRequest shoppingcartRequest) {
    return productService.setCompletedDate(shoppingcartRequest);
  }

  @PostMapping("/removeCartItem")
  public ResponseEntity<?> removeCartItem(@Valid @RequestBody CartRequest cartRequest) {
    return productService.removeCartItem(cartRequest);
  }

  @PostMapping("/updateShoppingCartItems")
  public ResponseEntity<?> updateShoppingCartItems(@Valid @RequestBody CartItemRequest cartItemRequest) {
    return productService.updateShoppingCartItems(cartItemRequest);
  }

}

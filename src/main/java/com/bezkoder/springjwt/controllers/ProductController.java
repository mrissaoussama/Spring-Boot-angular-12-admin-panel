package com.bezkoder.springjwt.controllers;

import javax.validation.Valid;

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

    @RequestMapping(value="/updateproductimage",consumes = {"multipart/mixed", "multipart/form-data"})
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<?> saveproductimage(
    @Valid @RequestPart("image") MultipartFile image,
    @Valid    @RequestParam("number") int number,
    @Valid    @RequestParam("id") long id,
    @Valid     @RequestPart("username") String username,
    @Valid    @RequestPart("password") String password) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return productService.saveProductImage(id, image, number);
    }
    @PostMapping("/findproduct")
    public ResponseEntity<?> findproduct(@Valid @RequestBody ProductRequest productRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(productRequest.getUsername(), productRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return productService.findProduct(productRequest);
    }


    @PostMapping("/addproduct")
    public ResponseEntity<?> addproduct(@Valid @RequestBody ProductRequest productRequest) {
//System.out.println(productRequest.toString());
//productRequest.getUsername(), productRequest.getPassword()

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(productRequest.getUsername(), productRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return productService.addProduct(productRequest);

    }
    @PostMapping("/updateproduct")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody ProductRequest productRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(productRequest.getUsername(), productRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return productService.updateProduct(productRequest);

    }
    @PostMapping("/deleteproduct")
    public ResponseEntity<?> deleteProduct(@Valid @RequestBody ProductRequest productRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(productRequest.getUsername(), productRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return productService.deleteProduct(productRequest);

    }
    @PostMapping("/addcategory")
    public ResponseEntity<?> addcategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(categoryRequest.getUsername(), categoryRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return productService.addcategory(categoryRequest);
    }
    @PostMapping("/deletecategory")
    public ResponseEntity<?> deletecategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(categoryRequest.getUsername(), categoryRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return productService.deletecategory(categoryRequest);
    }
    @PostMapping("/updatecategory")
    public ResponseEntity<?> updatecategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(categoryRequest.getUsername(), categoryRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return productService.updatecategory(categoryRequest);
    }
    @PostMapping("/getallcategories")
    public ResponseEntity<?> getAllCategories(@Valid @RequestBody CategoryRequest categoryRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(categoryRequest.getUsername(), categoryRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return productService.getAllCategories(categoryRequest);
    }

//shopping cart
@PostMapping("/addtocart")
public ResponseEntity<?> addtocart(@Valid @RequestBody CartRequest cartRequest) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(cartRequest.getUsername(), cartRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return productService.addToCart(cartRequest);
}
@PostMapping("/getallshoppingcarts")
public ResponseEntity<?> getallshoppingcarts(@Valid @RequestBody CartRequest cartRequest) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(cartRequest.getUsername(), cartRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return productService.getallshoppingcarts(cartRequest);
}
@PostMapping("/getshoppingcart")
public ResponseEntity<?> getShoppingcart(@Valid @RequestBody CartRequest cartRequest) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(cartRequest.getUsername(), cartRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return productService.getShoppingcart(cartRequest);
}
@PostMapping("/setshippingdate")
public ResponseEntity<?> setShippingDate(@Valid @RequestBody ShoppingCartRequest shoppingcartRequest) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(shoppingcartRequest.getUsername(), shoppingcartRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return productService.setShippingDate(shoppingcartRequest);
}
@PostMapping("/setcompleteddate")
public ResponseEntity<?> setCompletedDate(@Valid @RequestBody ShoppingCartRequest shoppingcartRequest) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(shoppingcartRequest.getUsername(), shoppingcartRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return productService.setCompletedDate(shoppingcartRequest);
}
}

package com.bezkoder.springjwt.controllers;

import javax.validation.Valid;

import com.bezkoder.springjwt.payload.request.LoginRequest;
import com.bezkoder.springjwt.payload.request.SignupRequest;
import com.bezkoder.springjwt.payload.request.UpdateRequest;
import com.bezkoder.springjwt.security.services.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api/auth")
public class AuthController {
@Autowired
UserDetailsServiceImpl userDetailsServiceImpl;
  @PostMapping("/getallusers")
  public ResponseEntity<?> getallusers(@Valid @RequestBody LoginRequest loginRequest) {
return userDetailsServiceImpl.getallusers(loginRequest);

  }

  @PostMapping("/deleteuser")
  public ResponseEntity<?> deleteUser(@Valid @RequestBody UpdateRequest updateRequest) {

   return userDetailsServiceImpl.deleteUser(updateRequest);
  }

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    return userDetailsServiceImpl.authenticateUser(loginRequest);
  }

  @PostMapping("/user-profile")
  public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateRequest updateRequest) {

    return userDetailsServiceImpl.updateUser(updateRequest);
  }

  @GetMapping("/user-profile")
  public ResponseEntity<?> getUserInfo(@Valid @RequestBody UpdateRequest updateRequest) {
return userDetailsServiceImpl.getUserInfo(updateRequest);
  }

  @RequestMapping("/updateprofileimage")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseBody
  public ResponseEntity<?> updateUserProfilePicture(@RequestPart("image") MultipartFile image,
      @RequestPart("username") String username, @RequestPart("password") String password) {

    return userDetailsServiceImpl.updateUserProfilePicture(image, username, password);


  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
   return userDetailsServiceImpl.registerUser(signUpRequest);
  }

  @GetMapping("/confirmuseraccount")
  public ResponseEntity<?> confirmUserAccount(@RequestParam("token") String confirmationToken) {
    return userDetailsServiceImpl.confirmUserAccount(confirmationToken);
  }
}

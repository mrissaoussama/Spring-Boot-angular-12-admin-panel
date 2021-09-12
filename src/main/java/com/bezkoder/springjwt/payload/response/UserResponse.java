package com.bezkoder.springjwt.payload.response;

import com.bezkoder.springjwt.models.User;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse {
  private String token;
  private String type = "Bearer";
  private User user;

  public UserResponse(String jwt, User user) {
    this.token = jwt;
    this.user = user;

  }
}

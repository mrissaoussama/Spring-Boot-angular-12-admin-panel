package com.bezkoder.springjwt.payload.response;

import java.util.List;

import com.bezkoder.springjwt.models.User;

import lombok.Data;

@Data
public class UserListResponse {

  private String token;
  private String type = "Bearer";
  private List<User> users;

  public UserListResponse(String jwt, List<User> users) {
    this.token = jwt;
    this.users = users;

  }

}

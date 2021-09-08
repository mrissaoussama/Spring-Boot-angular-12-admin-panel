package com.bezkoder.springjwt.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.validation.constraints.Size;

import com.bezkoder.springjwt.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsImpl implements UserDetails {
  private static final long serialVersionUID = 1L;

  private Long id;

  private String username;

  private String email;

  @JsonIgnore
  private String password;
  @Column(length = 2)
  @Nullable
  private int age;
  @Nullable
  private String name;
  @Nullable
  private String surname;

  @Nullable
  private String address;
  @Nullable
  private String city;
  @Nullable
  private String country;
  @Nullable
  private String job;
  @Nullable
  @Size(max = 600)
  private String description;
  @Nullable
  @Column(nullable = true)
  private boolean image;

  public void setId(Long id) {
    this.id = id;
  }

  private Collection<? extends GrantedAuthority> authorities;

  public UserDetailsImpl(Long id, String username, String email, String password,
      Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
    this.authorities = authorities;
  }

  public static UserDetailsImpl build(User user) {
    List<GrantedAuthority> authorities = user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());

    return new UserDetailsImpl(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), authorities);
  }

  public UserDetailsImpl(Long id, String username, String email, String password, int age, String name, String surname,
      String address, String city, String country, String job, String description, boolean image,
      Collection authorities) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
    this.age = age;
    this.name = name;
    this.surname = surname;
    this.address = address;
    this.city = city;
    this.country = country;
    this.job = job;
    this.description = description;
    this.image = image;
    this.authorities = authorities;
  }

  @Override
  public String toString() {
    return "{" + " id='" + getId() + "'" + ", username='" + getUsername() + "'" + ", email='" + getEmail() + "'"
        + ", password='" + getPassword() + "'" + ", age='" + getAge() + "'" + ", name='" + getName() + "'"
        + ", surname='" + getSurname() + "'" + ", address='" + getAddress() + "'" + ", city='" + getCity() + "'"
        + ", country='" + getCountry() + "'" + ", job='" + getJob() + "'" + ", description='" + getDescription() + "'"
        + ", image='" + isImage() + "'" + ", authorities='" + getAuthorities() + "'" + "}";
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    UserDetailsImpl user = (UserDetailsImpl) o;
    return Objects.equals(id, user.id);
  }
}

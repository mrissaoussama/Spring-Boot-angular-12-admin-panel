package com.bezkoder.springjwt.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import org.springframework.lang.Nullable;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "email") })
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
  @Column(unique=true)
	@NotBlank(message = "username must not be blank")
	@Size(max = 20,min = 3,message = "username must be between {min} and {max} characters")
	private String username;
  @Column(unique=true)
	@NotBlank(message = "email must not be blank")
	@Size(max = 50,message = "email must be shorter than {max} characters")
	@Email
	private String email;
@JsonIgnore
	@NotBlank(message = "password must not be blank")
	@Size(max = 60,min = 6,message = "password must be between {min} and {max} characters")
	private String password;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	@Column(length=2)
	@Nullable
	private int age;
	@Nullable
	private String name;
	@Nullable
	private String surname;
  @Column(name = "status")
  private String status="not activated";


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
  @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
  @JsonIdentityReference(alwaysAsId=true)  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "shoppingcart_id", referencedColumnName = "id")
  private ShoppingCart shoppingcart;
	public User() {
	}

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}
  public User(String username, String email, String password,Set<Role> roles) {
		this.username = username;
		this.email = email;
		this.password = password;
    this.roles=roles;
	}


	public String getPassword() {
return password;	}

	public void setPassword(String password) {
		this.password = password;
	}



}



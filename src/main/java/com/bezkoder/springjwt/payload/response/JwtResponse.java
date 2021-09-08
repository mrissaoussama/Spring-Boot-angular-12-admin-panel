package com.bezkoder.springjwt.payload.response;

import java.util.List;

import javax.persistence.Column;
import javax.validation.constraints.Size;

import org.springframework.lang.Nullable;

import lombok.Data;
@Data
public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private Long id;
	private String username;
	private String password;



	private String email;
	private List<String> roles;
public JwtResponse(String token, Long id, String username,
String password, String email,  int age, String name,
String surname, String address, String city, String country,
 String job, String description,boolean image,List<String> roles,String message) {
	this.token = token;
	this.id = id;
	this.username = username;
	this.password=password;
	this.email = email;
	this.roles = roles;
	this.age = age;
	this.name = name;
	this.surname = surname;
	this.address = address;
	this.city = city;
	this.country = country;
	this.job = job;
	this.description = description;
	this.image = image;
}

	public JwtResponse(String token, Long id, String username,
	String password, String email,  int age, String name,
	String surname, String address, String city, String country,
	 String job, String description,boolean image,List<String> roles) {
		this.token = token;
		this.id = id;
		this.username = username;
		this.password=password;
		this.email = email;
		this.roles = roles;
		this.age = age;
		this.name = name;
		this.surname = surname;
		this.address = address;
		this.city = city;
		this.country = country;
		this.job = job;
		this.description = description;
		this.image = image;
	}

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

	public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
	}

}

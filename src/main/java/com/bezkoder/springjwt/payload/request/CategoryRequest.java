package com.bezkoder.springjwt.payload.request;

import lombok.Data;
@Data
public class CategoryRequest {
	private String username;
	private String password;
	private Long id;
	private String name;
}

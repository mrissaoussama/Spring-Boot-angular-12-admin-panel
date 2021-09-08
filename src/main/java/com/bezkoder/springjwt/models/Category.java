package com.bezkoder.springjwt.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@ToString
@Setter
@Entity
@Table(	name = "Category")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
  @Column(unique=true)
  @Size(max = 20)
	private String name;


	public Category( String name) {
		this.name = name;
	}


	public Category(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Category() {
	}


}

package com.bezkoder.springjwt.payload.request;

import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.lang.Nullable;

import lombok.Data;
 @Data
public class UpdateRequest {
    @Id
    private Long id;
    @Size(min = 3, max = 20)
    private String username;

    @Size(max = 50)
    @Email
    private String email;
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

 @NotBlank
    @Size(min = 6, max = 40)
    private String password;
    @Override
    public String toString() {
        return "{" +
            " username='" + getUsername() + "'" +
            ", email='" + getEmail() + "'" +
            ", age='" + getAge() + "'" +
            ", name='" + getName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", address='" + getAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", country='" + getCountry() + "'" +
            ", job='" + getJob() + "'" +
            ", description='" + getDescription() + "'" +

            ", password='" + getPassword() + "'" +
            "}";
    }
}

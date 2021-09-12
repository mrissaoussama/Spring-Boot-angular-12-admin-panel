package com.bezkoder.springjwt;

import java.util.HashSet;
import java.util.Set;

import com.bezkoder.springjwt.models.Category;
import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Product;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.repository.CategoryRepository;
import com.bezkoder.springjwt.repository.ProductRepository;
import com.bezkoder.springjwt.repository.RoleRepository;
import com.bezkoder.springjwt.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
public class SpringBootSecurityJwtApplication {
  @Autowired
  PasswordEncoder encoder;

  public static void main(String[] args) {
    SpringApplication.run(SpringBootSecurityJwtApplication.class, args);
  }

  @Bean
  public CorsFilter corsFilter() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    final CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("*"); // this allows all origin
    config.addAllowedHeader("*"); // this allows all headers
    config.addAllowedMethod("OPTIONS");
    config.addAllowedMethod("HEAD");
    config.addAllowedMethod("GET");
    config.addAllowedMethod("PUT");
    config.addAllowedMethod("POST");
    config.addAllowedMethod("DELETE");
    config.addAllowedMethod("PATCH");
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

  @Bean
  CommandLineRunner init(RoleRepository RoleRepository, UserRepository userRepository,
      ProductRepository productRepository, CategoryRepository categoryRepository) {
    return args -> {
      Role r1 = new Role(ERole.ROLE_ADMIN);
      Role r2 = new Role(ERole.ROLE_MODERATOR);
      Role r3 = new Role(ERole.ROLE_USER);

      RoleRepository.save(r1);
      RoleRepository.save(r2);
      RoleRepository.save(r3);
      Set<Role> r = new HashSet<Role>();
      r.add(r1);
      r.add(r2);
      r.add(r3);
      User user = new User("mod", "mrissaoussama@gmail.com", encoder.encode("123456"), r);
      user.setStatus("Activated");
      userRepository.save(user);
      RoleRepository.findAll().forEach(System.out::println);
      userRepository.findAll().forEach(System.out::println);
      Category category = new Category("tech");
      Category category1 = new Category("pc");

      categoryRepository.save(category);
      categoryRepository.save(category1);

      Product product = new Product("ps5", "nice", category, 1F);
      Product product1 = new Product("a", "nice", category, 5F);
      Product product2 = new Product("z", "nice", category1, 7F);

      Product product3 = new Product("y", "hi", category, 8F);
      productRepository.save(product);
      productRepository.save(product1);
      productRepository.save(product2);
      productRepository.save(product3);

    };

  };

}

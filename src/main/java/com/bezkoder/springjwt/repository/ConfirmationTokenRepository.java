package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.ConfirmationToken;
import com.bezkoder.springjwt.models.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository
  extends JpaRepository<ConfirmationToken, Long> {

    ConfirmationToken findByConfirmationToken(String confirmationToken);
    ConfirmationToken findByUser(User user);
}

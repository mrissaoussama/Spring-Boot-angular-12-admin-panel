package com.bezkoder.springjwt.security.services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.bezkoder.springjwt.models.ConfirmationToken;
import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.LoginRequest;
import com.bezkoder.springjwt.payload.request.SignupRequest;
import com.bezkoder.springjwt.payload.request.UpdateRequest;
import com.bezkoder.springjwt.payload.response.JwtResponse;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.payload.response.UserResponse;
import com.bezkoder.springjwt.repository.ConfirmationTokenRepository;
import com.bezkoder.springjwt.repository.RoleRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.security.jwt.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  JwtUtils jwtUtils;
  @Autowired
  UserDetailsServiceImpl userDetailsServiceImpl;

  @Autowired
  PasswordEncoder encoder;
  @Autowired
  UserRepository userRepository;
  @Autowired
  RoleRepository roleRepository;
  @Autowired
  ProductService productService;
  @Autowired
  ConfirmationTokenRepository confirmationTokenRepository;
  @Autowired
  EmailSenderService emailSenderService;
  @Value("${upload.path}")
  private String uploadPath;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    return UserDetailsImpl.build(user);

  }

  public ResponseEntity<?> getallusers(LoginRequest loginRequest) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
        .collect(Collectors.toList());
    if (roles.contains("ROLE_ADMIN")) {
      return ResponseEntity.ok(new UserResponse(jwt, userRepository.findAll()));
    } else {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized access"));
    }
  }

  public ResponseEntity<?> deleteUser(UpdateRequest updateRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(updateRequest.getUsername(), updateRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
        .collect(Collectors.toList());
    if (roles.contains("ROLE_ADMIN") || updateRequest.getId() == userDetails.getId()) {
      userRepository.deleteById(updateRequest.getId());
      return ResponseEntity.ok(new MessageResponse("User deleted"));
    } else {
      return ResponseEntity.badRequest().body(new MessageResponse("Error deleting user: Unauthorized access"));
    }
  }

  public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
    User user = userRepository.findByUsernameIgnoreCase(loginRequest.getUsername());
    if (user==null||user.getStatus()=="Deleted")
    return ResponseEntity.badRequest().body(new MessageResponse("Error:Account does not exist"));
    if (user.getStatus()=="Banned")
    return ResponseEntity.badRequest().body(new MessageResponse("Error:Account Banned"));

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
        .collect(Collectors.toList());
     user = userRepository.findById(userDetails.getId()).get();
    if (user.getStatus() == "Activation pending")
    {SignupRequest signupRequest=new SignupRequest();
      signupRequest.setUsername(loginRequest.getUsername());
      signupRequest.setPassword(loginRequest.getPassword());
      this.sendNewConfirmationToken(signupRequest);
    return ResponseEntity.badRequest().body(new MessageResponse("Error: Account not verified, please check your email for a new activation link"));
    }

    if (user.getStatus() != "Activated")
      return ResponseEntity.badRequest().body(new MessageResponse("Error: please verify your account"));

    return ResponseEntity.ok(new JwtResponse(jwt, user.getId(), user.getUsername(), userDetails.getPassword(),
        user.getEmail(), user.getAge(), user.getName(), user.getSurname(), user.getAddress(), user.getCity(),
        user.getCountry(), user.getJob(), user.getDescription(), user.isImage(), roles));
  }

  public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {


    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }
    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }
    if(signUpRequest.getPassword().length()<6)
    return ResponseEntity.badRequest().body(new MessageResponse("Error: Password must not be empty or less than 6 characters"));

    User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
        encoder.encode(signUpRequest.getPassword()));
    Set<Role> roles = new HashSet<>();
    Role userRole = roleRepository.findByName(ERole.ROLE_USER).get();
    roles.add(userRole);
    user.setRoles(roles);
    user.setStatus("Activation pending");
    userRepository.save(user);

    return sendNewConfirmationToken(signUpRequest);
  }

  public ResponseEntity<?> sendNewConfirmationToken(SignupRequest signUpRequest) {
    User user = userRepository.findByUsernameIgnoreCase(signUpRequest.getUsername());
    if (user == null)
      return ResponseEntity.badRequest().body(new MessageResponse("Error: user does not exist"));
    ConfirmationToken token = confirmationTokenRepository.findByUser(user);
    if (token != null)
      confirmationTokenRepository.deleteById(token.getId());
    ConfirmationToken confirmationToken = new ConfirmationToken(user);
    confirmationTokenRepository.save(confirmationToken);
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(user.getEmail());
    mailMessage.setSubject("Complete Registration!");
    mailMessage.setFrom("mrissaoussama@gmail.com");
    mailMessage.setText("To activate your account, please click here : "
        + "http://localhost:4200/#/login?token=" + confirmationToken.getConfirmationToken());
    emailSenderService.sendEmail(mailMessage);

    return ResponseEntity.ok(new MessageResponse("verification link sent, please check your email to activate your account"));

  }

  public ResponseEntity<?> confirmUserAccount(String confirmationToken) {

    if (confirmationToken == null || confirmationToken == "")
      return ResponseEntity.badRequest().body(new MessageResponse("invalid token"));
    ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
    if (token != null) {
      User user = userRepository.findById(token.getUser().getId()).get();
      if ((user.getStatus() == "Deleted") || user == null)
        return ResponseEntity.ok(new MessageResponse("Account does not exist"));
      if (user.getStatus() == "Activated")
        return ResponseEntity.ok(new MessageResponse("Account already activated"));
      user.setStatus("Activated");
      userRepository.save(user);
      confirmationTokenRepository.deleteById(token.getId());
      return ResponseEntity.ok(new MessageResponse("Account Activated"));
    }

    return ResponseEntity.badRequest().body(new MessageResponse("Error Activating Account"));
  }

  public ResponseEntity<?> getUserInfo(UpdateRequest updateRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(updateRequest.getUsername(), updateRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String jwt = jwtUtils.generateJwtToken(authentication);
    List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
        .collect(Collectors.toList());
    User user = userRepository.findById(userDetails.getId()).get();

    return ResponseEntity.ok(new JwtResponse(jwt, user.getId(), user.getUsername(), userDetails.getPassword(),
        user.getEmail(), user.getAge(), user.getName(), user.getSurname(), user.getAddress(), user.getCity(),
        user.getCountry(), user.getJob(), user.getDescription(), user.isImage(), roles, "user updated"));
  }

  public ResponseEntity<?> updateUser(UpdateRequest updateRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(updateRequest.getUsername(), updateRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String jwt = jwtUtils.generateJwtToken(authentication);
    List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
        .collect(Collectors.toList());
    User user = userDetailsServiceImpl.saveUser(userDetails.getId(), updateRequest);
    return ResponseEntity.ok(new JwtResponse(jwt, user.getId(), user.getUsername(), userDetails.getPassword(),
        user.getEmail(), user.getAge(), user.getName(), user.getSurname(), user.getAddress(), user.getCity(),
        user.getCountry(), user.getJob(), user.getDescription(), user.isImage(), roles, "user updated"));
  }

  public User saveUser(long id, UpdateRequest updateRequest) {

    User user = userRepository.findById(id).get();
    if (updateRequest.getEmail() != null)
      user.setEmail(updateRequest.getEmail());
    if (updateRequest.getPassword() != null)
      user.setPassword(encoder.encode(updateRequest.getPassword()));
    user.setUsername(updateRequest.getUsername());
    user.setAddress(updateRequest.getAddress());
    user.setAge(updateRequest.getAge());
    user.setCity(updateRequest.getCity());
    user.setCountry(updateRequest.getCountry());
    user.setDescription(updateRequest.getDescription());
    user.setJob(updateRequest.getJob());
    user.setName(updateRequest.getName());
    user.setSurname(updateRequest.getSurname());
    return userRepository.save(user);
  }

  public void saveUserProfileImage(long id, MultipartFile image) {

    User user = userRepository.findById(id).get();
    try {
      Files.createDirectories(Paths.get("src/assets/userimages/" + id + "/"));
      OutputStream out = new FileOutputStream("src/assets/userimages/" + id + "/" + "profile" + ".jpg");
      out.write(image.getBytes());
      out.flush();
      out.close();
      user.setImage(true);
    } catch (IOException e) {
      e.printStackTrace();
    }
    userRepository.save(user);
  }

  public ResponseEntity<?> updateUserProfilePicture(MultipartFile image, String username, String password) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(username, password));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    this.saveUserProfileImage(userDetails.getId(), image);
    return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
  }

}

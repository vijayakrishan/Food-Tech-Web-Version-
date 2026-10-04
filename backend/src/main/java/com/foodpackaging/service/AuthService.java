package com.foodpackaging.service;
import com.foodpackaging.dto.*;
import com.foodpackaging.entity.*;
import com.foodpackaging.exception.BadRequestException;
import com.foodpackaging.repository.RoleRepository;
import com.foodpackaging.repository.UserRepository;
import com.foodpackaging.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .or(() -> userRepository.findByEmail(loginRequest.getUsername()))
                .orElseThrow(() -> new BadRequestException("User not found"));
        return AuthResponse.builder().token(jwt).id(user.getId()).username(user.getUsername()).email(user.getEmail()).role(user.getRole().getName().name()).build();
    }

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) { throw new BadRequestException("Username is already taken!"); }
        if (userRepository.existsByEmail(registerRequest.getEmail())) { throw new BadRequestException("Email is already in use!"); }
        Role userRole = roleRepository.findByName(ERole.valueOf(registerRequest.getRole() != null ? registerRequest.getRole() : "ROLE_PRODUCER"))
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        User user = User.builder().username(registerRequest.getUsername()).email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword())).firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName()).phone(registerRequest.getPhone()).organization(registerRequest.getOrganization())
                .role(userRole).isActive(true).build();
        userRepository.save(user);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(registerRequest.getUsername(), registerRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return AuthResponse.builder().token(jwt).id(user.getId()).username(user.getUsername()).email(user.getEmail()).role(user.getRole().getName().name()).build();
    }
}

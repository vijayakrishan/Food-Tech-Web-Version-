package com.foodpackaging.service;

import com.foodpackaging.entity.ERole;
import com.foodpackaging.entity.Role;
import com.foodpackaging.entity.User;
import com.foodpackaging.exception.BadRequestException;
import com.foodpackaging.exception.ResourceNotFoundException;
import com.foodpackaging.repository.RoleRepository;
import com.foodpackaging.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public User toggleUserActive(Long id) {
        User user = getUserById(id);
        user.setActive(!user.isActive());
        return userRepository.save(user);
    }

    @Transactional
    public User changeUserRole(Long id, String roleName) {
        User user = getUserById(id);
        try {
            ERole eRole = ERole.valueOf(roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName);
            Role role = roleRepository.findByName(eRole)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));
            user.setRole(role);
            return userRepository.save(user);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role specified: " + roleName);
        }
    }
}

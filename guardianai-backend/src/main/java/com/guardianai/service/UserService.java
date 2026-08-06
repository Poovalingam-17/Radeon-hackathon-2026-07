package com.guardianai.service;

import com.guardianai.dto.CreateUserRequest;
import com.guardianai.dto.UpdateUserRequest;
import com.guardianai.dto.UserDto;
import com.guardianai.model.Role;
import com.guardianai.model.User;
import com.guardianai.model.UserPrincipal;
import com.guardianai.repository.RoleRepository;
import com.guardianai.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Page<UserDto> findAll(Pageable pageable) {
        log.info("Fetching paginated users list");
        return userRepository.findAll(pageable).map(this::mapToUserDto);
    }

    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        log.info("Fetching user details for ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        return mapToUserDto(user);
    }

    @Transactional
    public UserDto create(CreateUserRequest request) {
        log.info("Creating user under administrative request: {}", request.getUsername());
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        Set<Role> roles = new HashSet<>();
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            for (String roleName : request.getRoles()) {
                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));
                roles.add(role);
            }
        } else {
            Role defaultRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));
            roles.add(defaultRole);
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .roles(roles)
                .build();

        return mapToUserDto(userRepository.save(user));
    }

    @Transactional
    public UserDto update(Long id, UpdateUserRequest request) {
        log.info("Updating user details for ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));

        if (!user.getUsername().equals(request.getUsername()) && userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        return mapToUserDto(userRepository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public UserDto changeStatus(Long id, boolean enabled) {
        log.info("Toggling active status for user ID: {} to {}", id, enabled);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        user.setEnabled(enabled);
        return mapToUserDto(userRepository.save(user));
    }

    @Transactional
    public UserDto assignRole(Long userId, Long roleId) {
        log.info("Assigning role ID: {} to user ID: {}", roleId, userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + roleId));

        user.getRoles().add(role);
        return mapToUserDto(userRepository.save(user));
    }

    @Transactional
    public UserDto removeRole(Long userId, Long roleId) {
        log.info("Removing role ID: {} from user ID: {}", roleId, userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + roleId));

        user.getRoles().remove(role);
        return mapToUserDto(userRepository.save(user));
    }

    @Transactional
    public UserDto updateProfile(UpdateUserRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User must be authenticated to update profile");
        }

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return update(principal.getId(), request);
    }

    @Transactional(readOnly = true)
    public UserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrincipal userPrincipal) {
            User user = userRepository.findById(userPrincipal.getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            return mapToUserDto(user);
        }
        return null;
    }

    private UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .build();
    }
}

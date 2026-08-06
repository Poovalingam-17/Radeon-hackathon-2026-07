package com.guardianai.config;

import com.guardianai.model.Permission;
import com.guardianai.model.Role;
import com.guardianai.model.User;
import com.guardianai.repository.PermissionRepository;
import com.guardianai.repository.RoleRepository;
import com.guardianai.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository, RoleRepository roleRepository,
                            PermissionRepository permissionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Checking database initialization status...");

        // 1. Ensure permissions exist
        List<String> permissionNames = Arrays.asList(
                "CREATE_POLICY", "READ_POLICY", "UPDATE_POLICY", "DELETE_POLICY",
                "READ_AUDIT", "MANAGE_USERS", "MANAGE_AGENTS", "EXECUTE_TASK"
        );

        Set<Permission> allPermissions = new HashSet<>();
        for (String permName : permissionNames) {
            Permission permission = permissionRepository.findByName(permName)
                    .orElseGet(() -> permissionRepository.save(Permission.builder().name(permName).build()));
            allPermissions.add(permission);
        }

        // 2. Ensure roles exist
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> {
                    Role role = Role.builder()
                            .name("ROLE_ADMIN")
                            .permissions(allPermissions)
                            .build();
                    return roleRepository.save(role);
                });

        roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role role = Role.builder()
                            .name("ROLE_USER")
                            .build();
                    return roleRepository.save(role);
                });

        // 3. Ensure primary admin user exists
        if (userRepository.count() == 0) {
            log.info("No users found in database. Initializing default administrator account...");
            User admin = User.builder()
                    .username("admin")
                    .email("admin@guardian.ai")
                    .password(passwordEncoder.encode("admin1234"))
                    .enabled(true)
                    .roles(new HashSet<>(Arrays.asList(adminRole)))
                    .build();

            userRepository.save(admin);
            log.info("Default administrator account created successfully.");
            log.info("Credentials -> Username: admin / Email: admin@guardian.ai / Password: admin1234");
        } else {
            log.info("Database already initialized with users.");
        }
    }
}

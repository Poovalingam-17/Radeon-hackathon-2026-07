package com.guardianai.service;

import com.guardianai.dto.RoleDto;
import com.guardianai.model.Permission;
import com.guardianai.model.Role;
import com.guardianai.repository.PermissionRepository;
import com.guardianai.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Transactional(readOnly = true)
    public List<RoleDto> findAll() {
        log.info("Fetching all roles");
        return roleRepository.findAll().stream().map(this::mapToRoleDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoleDto findById(Long id) {
        log.info("Fetching role details for ID: {}", id);
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + id));
        return mapToRoleDto(role);
    }

    @Transactional
    public RoleDto create(RoleDto dto) {
        log.info("Creating role: {}", dto.getName());
        if (roleRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Role name is already taken");
        }

        Role role = Role.builder()
                .name(dto.getName())
                .build();

        return mapToRoleDto(roleRepository.save(role));
    }

    @Transactional
    public RoleDto update(Long id, RoleDto dto) {
        log.info("Updating role ID: {}", id);
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + id));

        if (!role.getName().equals(dto.getName()) && roleRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Role name is already taken");
        }

        role.setName(dto.getName());
        return mapToRoleDto(roleRepository.save(role));
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting role with ID: {}", id);
        if (!roleRepository.existsById(id)) {
            throw new IllegalArgumentException("Role not found with ID: " + id);
        }
        roleRepository.deleteById(id);
    }

    @Transactional
    public RoleDto assignPermission(Long roleId, Long permissionId) {
        log.info("Assigning permission ID: {} to role ID: {}", permissionId, roleId);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + roleId));
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found with ID: " + permissionId));

        role.getPermissions().add(permission);
        return mapToRoleDto(roleRepository.save(role));
    }

    @Transactional
    public RoleDto removePermission(Long roleId, Long permissionId) {
        log.info("Removing permission ID: {} from role ID: {}", permissionId, roleId);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + roleId));
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found with ID: " + permissionId));

        role.getPermissions().remove(permission);
        return mapToRoleDto(roleRepository.save(role));
    }

    private RoleDto mapToRoleDto(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .permissions(role.getPermissions().stream().map(Permission::getName).collect(Collectors.toSet()))
                .build();
    }
}

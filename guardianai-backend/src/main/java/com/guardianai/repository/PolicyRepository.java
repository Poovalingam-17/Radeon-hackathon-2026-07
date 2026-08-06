package com.guardianai.repository;

import com.guardianai.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {
    List<Policy> findByCreatedBy(String username);
    Optional<Policy> findByName(String name);
    boolean existsByName(String name);
}

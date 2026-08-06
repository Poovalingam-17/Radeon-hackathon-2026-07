package com.guardianai.repository;

import com.guardianai.model.Agent;
import com.guardianai.model.AgentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    Optional<Agent> findByType(AgentType type);
}

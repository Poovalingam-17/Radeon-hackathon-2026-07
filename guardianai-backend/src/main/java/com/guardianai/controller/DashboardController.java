package com.guardianai.controller;

import com.guardianai.dto.DashboardActivityDto;
import com.guardianai.dto.DashboardStatsDto;
import com.guardianai.model.RiskScore;
import com.guardianai.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDto> getStats() {
        return ResponseEntity.ok(dashboardService.getStats());
    }

    @GetMapping("/activities")
    public ResponseEntity<List<DashboardActivityDto>> getActivities() {
        return ResponseEntity.ok(dashboardService.getActivities());
    }

    @GetMapping("/risk-scores")
    public ResponseEntity<List<RiskScore>> getRiskScores() {
        return ResponseEntity.ok(dashboardService.getRiskScores());
    }

    @GetMapping("/compliance")
    public ResponseEntity<Map<String, Object>> getCompliance() {
        return ResponseEntity.ok(dashboardService.getComplianceStats());
    }

    @GetMapping("/agent-status")
    public ResponseEntity<List<Map<String, Object>>> getAgentStatus() {
        return ResponseEntity.ok(dashboardService.getAgentStatusList());
    }
}

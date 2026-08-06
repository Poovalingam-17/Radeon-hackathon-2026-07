package com.guardianai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyDto {
    private Long id;
    private String name;
    private String description;
    private String type;
    private String status;
    private int priority;
    private String version;
    private String createdBy;
    private List<RuleDto> rules;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RuleDto {
        private Long id;
        private String name;
        private String description;
        private String condition;
        private String action;
        private int priority;
        private boolean enabled;
    }
}

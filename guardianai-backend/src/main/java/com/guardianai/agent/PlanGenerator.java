package com.guardianai.agent;

import com.guardianai.dto.PlanResponse.PlanStepDto;
import com.guardianai.model.AgentType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlanGenerator {

    public List<PlanStepDto> generateSteps(IntentAnalyzer.AnalysisResult analysis) {
        List<PlanStepDto> steps = new ArrayList<>();

        steps.add(PlanStepDto.builder()
                .orderIndex(1)
                .agentType(AgentType.SECURITY)
                .action("CHECK_AUTHORIZATION")
                .description("Validate JWT session, inspect credentials, and assess user risk profile.")
                .build());

        steps.add(PlanStepDto.builder()
                .orderIndex(2)
                .agentType(AgentType.MEMORY)
                .action("RETRIEVE_CONTEXT")
                .description("Fetch session conversation history and execute semantic check for similar target resources.")
                .build());

        steps.add(PlanStepDto.builder()
                .orderIndex(3)
                .agentType(AgentType.EXECUTION)
                .action("EVALUATE_POLICIES")
                .description("Execute compliance engine rules against target " + analysis.entityType() + " schema.")
                .build());

        steps.add(PlanStepDto.builder()
                .orderIndex(4)
                .agentType(AgentType.EXECUTION)
                .action(analysis.action().toUpperCase() + "_" + analysis.entityType().toUpperCase())
                .description("Commit resource modification transaction: " + analysis.action() + " " + analysis.entityType() + ".")
                .build());

        steps.add(PlanStepDto.builder()
                .orderIndex(5)
                .agentType(AgentType.EXECUTION)
                .action("LOG_AUDIT_TRAIL")
                .description("Save telemetry trail to audit logs and distribute alerts if anomalous activity is flagged.")
                .build());

        return steps;
    }
}

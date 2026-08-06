package com.guardianai.agent;

import com.guardianai.dto.PlanResponse;
import com.guardianai.dto.PlanResponse.PlanStepDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class PlannerAgent {

    private final IntentAnalyzer intentAnalyzer;
    private final PlanGenerator planGenerator;
    private final PlanValidator planValidator;
    private final PlanOptimizer planOptimizer;

    public PlannerAgent(IntentAnalyzer intentAnalyzer, PlanGenerator planGenerator,
                        PlanValidator planValidator, PlanOptimizer planOptimizer) {
        this.intentAnalyzer = intentAnalyzer;
        this.planGenerator = planGenerator;
        this.planValidator = planValidator;
        this.planOptimizer = planOptimizer;
    }

    public PlanResponse plan(String prompt) {
        log.info("Planner Agent starting execution for request: '{}'", prompt);

        // Step 1: Intent Analysis
        IntentAnalyzer.AnalysisResult analysis = intentAnalyzer.analyze(prompt);
        log.info("Extracted Intent: {} with confidence {}", analysis.intent(), analysis.confidence());

        // Step 2: Plan Steps Generation
        List<PlanStepDto> steps = planGenerator.generateSteps(analysis);

        // Step 3: Plan Validation
        PlanResponse plan = PlanResponse.builder()
                .intent(analysis.intent())
                .confidence(analysis.confidence())
                .steps(steps)
                .status("GENERATED")
                .build();

        boolean isValid = planValidator.validate(plan);
        if (!isValid) {
            log.warn("Generated plan structural check failed! Marking status as INVALID.");
            plan.setStatus("INVALID");
            return plan;
        }

        // Step 4: Plan Optimization
        PlanResponse optimizedPlan = planOptimizer.optimize(plan);
        log.info("Planner Agent finished successfully. Plan Status: {}", optimizedPlan.getStatus());

        return optimizedPlan;
    }
}

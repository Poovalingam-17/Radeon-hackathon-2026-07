package com.guardianai.agent;

import com.guardianai.llm.LLMProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class IntentAnalyzer {

    private final LLMProvider llmProvider;
    private final ObjectMapper objectMapper;

    public IntentAnalyzer(LLMProvider llmProvider) {
        this.llmProvider = llmProvider;
        this.objectMapper = new ObjectMapper();
    }

    public AnalysisResult analyze(String prompt) {
        log.info("Analyzing user intent via local LLM for prompt: '{}'", prompt);

        String systemInstruction = """
                You are an intent analyzer for GuardianAI, an enterprise governance platform.
                Analyze the user request and respond strictly in JSON format. Do not return any other text.
                
                The JSON must contain these fields:
                - "intent": The primary action class (e.g., "CREATE_POLICY", "EVALUATE_COMPLIANCE", "VIEW_AUDIT_LOGS", "SCAN_THREATS", "UNKNOWN").
                - "confidence": Float between 0.0 and 1.0 representing your confidence.
                - "entityType": The entity targeted (e.g., "policy", "user", "audit", "agent").
                - "action": Specific action verb (e.g., "create", "read", "evaluate", "list").
                
                Example Output:
                {"intent": "CREATE_POLICY", "confidence": 0.95, "entityType": "policy", "action": "create"}
                
                User request: "%s"
                """;

        String formattedPrompt = String.format(systemInstruction, prompt);
        try {
            String llmResponse = llmProvider.generate(formattedPrompt);
            log.debug("LLM Response: {}", llmResponse);

            String cleanedResponse = cleanJsonResponse(llmResponse);
            return objectMapper.readValue(cleanedResponse, AnalysisResult.class);
        } catch (Exception e) {
            log.error("Failed to parse intent via LLM: {}. Falling back to rule-based heuristics.", e.getMessage());
            return heuristicFallback(prompt);
        }
    }

    private String cleanJsonResponse(String response) {
        String cleaned = response.trim();
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
        }
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3);
        }
        return cleaned.trim();
    }

    private AnalysisResult heuristicFallback(String prompt) {
        String upperPrompt = prompt.toUpperCase();
        if (upperPrompt.contains("POLICY") && (upperPrompt.contains("CREATE") || upperPrompt.contains("NEW") || upperPrompt.contains("ADD"))) {
            return new AnalysisResult("CREATE_POLICY", 0.8, "policy", "create");
        }
        if (upperPrompt.contains("AUDIT") || upperPrompt.contains("LOG") || upperPrompt.contains("HISTORY")) {
            return new AnalysisResult("VIEW_AUDIT_LOGS", 0.75, "audit", "read");
        }
        if (upperPrompt.contains("THREAT") || upperPrompt.contains("SCAN") || upperPrompt.contains("RISK")) {
            return new AnalysisResult("SCAN_THREATS", 0.8, "agent", "scan");
        }
        return new AnalysisResult("UNKNOWN", 0.5, "unknown", "unknown");
    }

    public record AnalysisResult(String intent, double confidence, String entityType, String action) {
        public AnalysisResult() {
            this("UNKNOWN", 0.0, "unknown", "unknown");
        }
    }
}

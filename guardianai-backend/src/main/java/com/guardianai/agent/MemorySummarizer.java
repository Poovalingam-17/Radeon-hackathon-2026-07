package com.guardianai.agent;

import com.guardianai.llm.LLMProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MemorySummarizer {

    private final LLMProvider llmProvider;

    public MemorySummarizer(LLMProvider llmProvider) {
        this.llmProvider = llmProvider;
    }

    public String summarize(List<ShortTermMemory.Message> messages) {
        if (messages == null || messages.isEmpty()) {
            return "No previous conversation context.";
        }

        log.info("Summarizing short-term conversation context of {} items", messages.size());

        String chatLog = messages.stream()
                .map(m -> m.role() + ": " + m.content())
                .collect(Collectors.joining("\n"));

        String instruction = """
                You are a context compression assistant.
                Summarize the following conversation logs into a concise context description.
                Do not add any greetings or surrounding explanations, just return the summary.
                
                Logs:
                %s
                """;

        try {
            return llmProvider.generate(String.format(instruction, chatLog)).trim();
        } catch (Exception e) {
            log.error("Failed to generate summary via LLM: {}", e.getMessage());
            return messages.stream()
                    .skip(Math.max(0, messages.size() - 2))
                    .map(ShortTermMemory.Message::content)
                    .collect(Collectors.joining(" | "));
        }
    }
}

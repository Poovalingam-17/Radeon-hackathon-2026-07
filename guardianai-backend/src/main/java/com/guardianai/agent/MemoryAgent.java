package com.guardianai.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class MemoryAgent {

    private final ShortTermMemory shortTermMemory;
    private final LongTermMemory longTermMemory;
    private final VectorMemory vectorMemory;
    private final MemorySummarizer memorySummarizer;

    public MemoryAgent(ShortTermMemory shortTermMemory, LongTermMemory longTermMemory,
                       VectorMemory vectorMemory, MemorySummarizer memorySummarizer) {
        this.shortTermMemory = shortTermMemory;
        this.longTermMemory = longTermMemory;
        this.vectorMemory = vectorMemory;
        this.memorySummarizer = memorySummarizer;
    }

    public MemoryContext retrieveContext(Long userId, String prompt) {
        log.info("Memory Agent retrieving context for user ID: {}", userId);

        List<ShortTermMemory.Message> history = shortTermMemory.getHistory(userId);
        String shortTermSummary = memorySummarizer.summarize(history);
        List<VectorMemory.SearchResult> vectorHits = vectorMemory.search(prompt, 3, 0.4);
        List<String> longTermHistories = longTermMemory.getHistoricalContext(userId);

        shortTermMemory.appendMessage(userId, "user", prompt);
        vectorMemory.addDocument(UUID.randomUUID().toString(), prompt);

        return new MemoryContext(shortTermSummary, vectorHits, longTermHistories);
    }

    public record MemoryContext(String shortTermSummary, List<VectorMemory.SearchResult> semanticMatches, List<String> longTermLogs) {}
}

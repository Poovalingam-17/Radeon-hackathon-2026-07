package com.guardianai.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class VectorMemory {

    private final List<MemoryDocument> documents = new ArrayList<>();

    public void addDocument(String id, String content) {
        documents.add(new MemoryDocument(id, content));
        log.info("Document added to local vector store. ID: {}", id);
    }

    public List<SearchResult> search(String query, int limit, double threshold) {
        log.info("Semantic vector search triggered for query: '{}'", query);
        List<SearchResult> results = new ArrayList<>();

        for (MemoryDocument doc : documents) {
            double score = calculateCosineSimilarity(query, doc.content());
            if (score >= threshold) {
                results.add(new SearchResult(doc.id(), doc.content(), score));
            }
        }

        results.sort((a, b) -> Double.compare(b.score(), a.score()));
        return results.stream().limit(limit).toList();
    }

    private double calculateCosineSimilarity(String text1, String text2) {
        Map<String, Integer> freq1 = getWordFrequencies(text1);
        Map<String, Integer> freq2 = getWordFrequencies(text2);

        Set<String> allWords = new HashSet<>(freq1.keySet());
        allWords.addAll(freq2.keySet());

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (String word : allWords) {
            int v1 = freq1.getOrDefault(word, 0);
            int v2 = freq2.getOrDefault(word, 0);

            dotProduct += v1 * v2;
            norm1 += Math.pow(v1, 2);
            norm2 += Math.pow(v2, 2);
        }

        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    private Map<String, Integer> getWordFrequencies(String text) {
        Map<String, Integer> freqs = new HashMap<>();
        String[] words = text.toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", "").split("\\s+");
        for (String word : words) {
            if (word.trim().isEmpty()) continue;
            freqs.put(word, freqs.getOrDefault(word, 0) + 1);
        }
        return freqs;
    }

    public record MemoryDocument(String id, String content) {}
    public record SearchResult(String id, String content, double score) {}
}

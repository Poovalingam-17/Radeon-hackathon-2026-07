package com.guardianai.llm;

import dev.langchain4j.model.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

@Service
public class OllamaLLMProvider implements LLMProvider {

    private final OllamaChatModel ollamaChatModel;

    public OllamaLLMProvider(OllamaChatModel ollamaChatModel) {
        this.ollamaChatModel = ollamaChatModel;
    }

    @Override
    public String generate(String prompt) {
        try {
            return ollamaChatModel.generate(prompt);
        } catch (Exception e) {
            throw new RuntimeException("Error communicating with local LLM (Ollama): " + e.getMessage(), e);
        }
    }
}

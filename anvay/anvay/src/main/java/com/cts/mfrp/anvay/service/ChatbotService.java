package com.cts.mfrp.anvay.service;

import com.cts.mfrp.anvay.dto.ChatbotEntry;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatbotService {

    private static final String FALLBACK =
            "I can answer only the queries regarding the multi-tenant pod 2.";
    private static final String KNOWLEDGE_BASE_FILE = "chatbot_responses.json";

    private final ObjectMapper objectMapper;

    private List<ChatbotEntry> knowledgeBase = new ArrayList<>();

    @PostConstruct
    public void loadKnowledgeBase() {
        try {
            ClassPathResource resource = new ClassPathResource(KNOWLEDGE_BASE_FILE);
            knowledgeBase = objectMapper.readValue(
                    resource.getInputStream(),
                    new TypeReference<List<ChatbotEntry>>() {}
            );
            log.info("Chatbot knowledge base loaded: {} categories from {}",
                    knowledgeBase.size(), KNOWLEDGE_BASE_FILE);
        } catch (IOException e) {
            log.error("Could not load chatbot_responses.json — bot will use fallback only. Error: {}",
                    e.getMessage());
        }
    }

    public String processMessage(String userMessage) {
        if (userMessage == null || userMessage.isBlank()) {
            return FALLBACK;
        }

        // Normalise: lowercase + collapse punctuation to spaces
        String normalized = userMessage
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();

        for (ChatbotEntry entry : knowledgeBase) {
            for (String keyword : entry.getKeywords()) {
                String kw = keyword.toLowerCase().trim();
                // Whole-word or substring match depending on keyword length
                if (kw.length() <= 4) {
                    // Short keywords (e.g. "reg ") — match only as whole word
                    if (normalized.matches(".*\\b" + java.util.regex.Pattern.quote(kw.trim()) + "\\b.*")) {
                        log.debug("Matched category '{}' via keyword '{}'", entry.getCategory(), kw);
                        return entry.getResponse();
                    }
                } else {
                    // Longer keywords — substring match is safe enough
                    if (normalized.contains(kw)) {
                        log.debug("Matched category '{}' via keyword '{}'", entry.getCategory(), kw);
                        return entry.getResponse();
                    }
                }
            }
        }

        log.debug("No keyword match for: '{}'", normalized);
        return FALLBACK;
    }
}

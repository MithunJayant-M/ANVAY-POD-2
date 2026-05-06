package com.cts.mfrp.anvay.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ChatbotEntry {
    private String category;
    private List<String> keywords;
    private String response;
}

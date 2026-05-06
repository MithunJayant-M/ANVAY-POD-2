package com.cts.mfrp.anvay.controller;

import com.cts.mfrp.anvay.dto.ChatRequest;
import com.cts.mfrp.anvay.dto.ChatResponse;
import com.cts.mfrp.anvay.service.ChatbotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Tag(
    name = "Anvay Support Bot",
    description = "Rule-based keyword chatbot strictly scoped to Pod 2 platform queries. " +
                  "No LLM or external API. All responses served from chatbot_responses.json."
)
public class ChatController {

    private final ChatbotService chatbotService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Send a message to Anvay Support Bot (Offline Mode)",
        description = """
            Processes a user message using keyword matching against the Pod 2 knowledge base.
            Recognised topics: event registration, club leadership, event creation, event rules,
            clubs, points, leaderboard, profile, password reset, winners, dashboard navigation.
            Any message with no matching keyword returns the exact fallback:
            "I can answer only the queries regarding the multi-tenant pod 2."
            The knowledge base is loaded from `chatbot_responses.json` on startup and can be
            edited without recompiling the application (requires server restart).
            """,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Bot reply returned successfully",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ChatResponse.class),
                    examples = @ExampleObject(
                        name = "register-example",
                        value = """
                            {"reply": "To register for an event on Anvay: 1. Go to Events in your Student Dashboard ..."}
                            """
                    )
                )
            ),
            @ApiResponse(responseCode = "400", description = "Empty or too-long message")
        }
    )
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        String reply = chatbotService.processMessage(request.getMessage());
        return ResponseEntity.ok(new ChatResponse(reply));
    }
}

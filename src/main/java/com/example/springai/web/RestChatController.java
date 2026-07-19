package com.example.springai.web;

import com.example.springai.middleware.model.request.ChatMessageRequest;
import com.example.springai.middleware.model.response.ChatResponse;
import com.example.springai.middleware.model.response.ChatShortResponse;
import com.example.springai.middleware.service.ChatEdgeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.UUID;

@Service
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class RestChatController {

    private final ChatEdgeService chatEdgeService;

    @Value("${app.user.default.id}")
    private UUID defaultUserId;

    @GetMapping(value = {"/{chat_id}", "/{chat_id}/"})
    public ResponseEntity<ChatResponse> getChat(@Valid @NotBlank @PathVariable("chat_id") final String chatIdText) {
        UUID chatId = UUID.fromString(chatIdText);
        ChatResponse chat = chatEdgeService.getChat(chatId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(chat);
    }

    @GetMapping(value = {"/archive", "/archive/"})
    public ResponseEntity<List<ChatShortResponse>> getArchivedChats() {
        List<ChatShortResponse> archivedChats = chatEdgeService.getAllUserArchivedChats(defaultUserId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(archivedChats);
    }

    @PatchMapping(value = {"/{chat_id}/title", "/{chat_id}/title/"})
    public void updateChatTitle(@Valid @NotBlank @PathVariable("chat_id") final String chatIdText,
                                @Valid @NotBlank @RequestParam("title") final String title
    ) {
        UUID chatId = UUID.fromString(chatIdText);
        chatEdgeService.updateChatTitle(chatId, title);
    }

    @PostMapping(value = {"/{chat_id}/message_stream", "/{chat_id}/message_stream/"}, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter processMessage(@Valid @PathVariable("chat_id") @NotNull final String chatIdText,
                                     @Valid @RequestBody @NotNull final ChatMessageRequest request
    ) {
        UUID chatId = UUID.fromString(chatIdText);
        return chatEdgeService.processMessageWithStreaming(chatId, request.content());
    }

}

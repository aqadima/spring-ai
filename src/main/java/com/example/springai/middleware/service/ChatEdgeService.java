package com.example.springai.middleware.service;

import com.example.springai.middleware.model.request.ChatRequest;
import com.example.springai.middleware.model.response.ChatResponse;
import com.example.springai.middleware.model.response.ChatShortResponse;
import jakarta.annotation.Nonnull;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.UUID;

public interface ChatEdgeService {

    @Nonnull
    UUID createNewChat(@Nonnull final UUID userId,
                       @Nonnull final ChatRequest request);

    @Nonnull
    ChatResponse getChat(@Nonnull final UUID chatId);

    @Nonnull
    List<ChatShortResponse> getAllUserActiveChats(@Nonnull final UUID userId);

    @Nonnull
    List<ChatShortResponse> getAllUserArchivedChats(@Nonnull final UUID userId);

    void updateChatTitle(@Nonnull final UUID chatId,
                         @Nonnull final String title);

    void archiveChat(@Nonnull final UUID chatId);

    void unarchiveChat(@Nonnull final UUID chatId);

    void deleteChat(@Nonnull final UUID chatId);

    @Nonnull
    SseEmitter processMessageWithStreaming(@Nonnull final UUID chatId,
                                           @Nonnull final String prompt);
}

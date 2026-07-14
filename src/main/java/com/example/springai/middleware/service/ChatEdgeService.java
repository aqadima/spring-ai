package com.example.springai.middleware.service;

import com.example.springai.middleware.model.request.ChatRequest;
import com.example.springai.middleware.model.response.ChatResponse;
import com.example.springai.middleware.model.response.ChatShortResponse;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.UUID;

public interface ChatEdgeService {

    @Nonnull
    UUID createNewChat(UUID userId, ChatRequest request);

    @Nonnull
    ChatResponse getChat(@Nonnull final UUID chatId);

    @Nonnull
    List<ChatShortResponse> getAllUserChats(@Nonnull final UUID userId);

    void deleteChat(UUID chatId);

    @Nonnull
    String sendChatMessage(@Nonnull final UUID chatId, @Nonnull String prompt);

}

package com.example.springai.domain.service;

import com.example.springai.domain.model.ChatEntity;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.UUID;

public interface ChatDomainService {

    @Nonnull
    ChatEntity save(@Nonnull final ChatEntity chatEntity);

    @Nonnull
    ChatEntity getChatById(@Nonnull final UUID chatId);

    @Nonnull
    List<ChatEntity> getAllUserActiveChats(@Nonnull final UUID userId);

    @Nonnull
    List<ChatEntity> getAllUserArchivedChats(@Nonnull final UUID userId);

    void deleteChat(@Nonnull final UUID chatId);

}

package com.example.springai.domain.service;

import com.example.springai.domain.model.ChatMessageEntity;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatMessageDomainService {

    @Nonnull
    ChatMessageEntity save(@Nonnull final ChatMessageEntity chatMessageEntity);

    @Nonnull
    Optional<Long> getLastMessageNumberOfChat(@Nonnull UUID chatId);

    @Nonnull
    ChatMessageEntity getById(@Nonnull final UUID chatMessageId);

    @Nonnull
    List<ChatMessageEntity> getAllByChatId(@Nonnull final UUID chatId);

    void deleteMessage(@Nonnull final UUID messageId);

}

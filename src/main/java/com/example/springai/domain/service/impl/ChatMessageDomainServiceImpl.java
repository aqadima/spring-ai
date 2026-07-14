package com.example.springai.domain.service.impl;

import com.example.springai.domain.enums.ResourceType;
import com.example.springai.domain.model.ChatMessageEntity;
import com.example.springai.domain.repository.ChatMessageRepository;
import com.example.springai.domain.service.ChatMessageDomainService;
import com.example.springai.exception.ResourceNotFoundException;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatMessageDomainServiceImpl implements ChatMessageDomainService {

    private final ChatMessageRepository chatMessageRepository;

    @Nonnull
    @Override
    public ChatMessageEntity save(@Nonnull final ChatMessageEntity chatMessageEntity) {
        return chatMessageRepository.save(chatMessageEntity);
    }

    @Nonnull
    @Override
    public Optional<Long> getLastMessageNumberOfChat(@Nonnull final UUID chatId) {
        return chatMessageRepository.getLastChatMessageNumberByChatId(chatId);
    }

    @Nonnull
    @Override
    public ChatMessageEntity getById(@Nonnull final UUID chatMessageId) {
        return chatMessageRepository.findById(chatMessageId)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceType.CHAT_MESSAGE, chatMessageId.toString()));
    }

    @Nonnull
    @Override
    public List<ChatMessageEntity> getAllByChatId(@Nonnull final UUID chatId) {
        return chatMessageRepository.findAllByChatId(chatId);
    }

    @Override
    public void deleteMessage(@Nonnull final UUID messageId) {
        chatMessageRepository.deleteById(messageId);
    }
}

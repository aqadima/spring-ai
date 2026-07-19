package com.example.springai.domain.service.impl;

import com.example.springai.domain.enums.ResourceType;
import com.example.springai.domain.model.ChatEntity;
import com.example.springai.domain.repository.ChatRepository;
import com.example.springai.domain.service.ChatDomainService;
import com.example.springai.exception.ResourceNotFoundException;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatDomainServiceImpl implements ChatDomainService {

    private final ChatRepository chatRepository;

    @Nonnull
    @Override
    public ChatEntity save(@Nonnull final ChatEntity chat) {
        return chatRepository.save(chat);
    }

    @Nonnull
    @Override
    public ChatEntity getChatById(@Nonnull final UUID chatId) {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceType.CHAT, chatId.toString()));
    }

    @Nonnull
    @Override
    public List<ChatEntity> getAllUserActiveChats(@Nonnull final UUID userId) {
        return chatRepository.findAllActiveChatsByUserId(userId);
    }

    @Nonnull
    @Override
    public List<ChatEntity> getAllUserArchivedChats(@Nonnull final UUID userId) {
        return chatRepository.findAllArchivedChatsByUserId(userId);
    }

    @Override
    public void deleteChat(@Nonnull final UUID chatId) {
        chatRepository.findById(chatId)
                .ifPresentOrElse(
                        _ -> chatRepository.deleteById(chatId),
                        () -> {
                            throw new ResourceNotFoundException(ResourceType.CHAT, chatId.toString());
                        }
                );
    }

}

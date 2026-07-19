package com.example.springai.middleware.service;

import com.example.springai.domain.model.ChatEntity;
import com.example.springai.domain.model.ChatMessageEntity;
import com.example.springai.domain.service.ChatDomainService;
import com.example.springai.domain.service.ChatMessageDomainService;
import com.example.springai.middleware.mapper.ChatMessageMapper;
import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Builder
@RequiredArgsConstructor
public class PostgresChatMemory implements ChatMemory {

    private final ChatDomainService chatDomainService;
    private final ChatMessageDomainService chatMessageDomainService;
    private final ChatMessageMapper chatMessageMapper;

    private final int maxMessages;

    @Override
    @Transactional
    public void add(@Nonnull final String conversationId, @Nonnull final List<Message> messages) {
        UUID chatId = UUID.fromString(conversationId);
        ChatEntity chat = chatDomainService.getChatById(chatId);
        AtomicLong lastMessageNumber = new AtomicLong(
                chatMessageDomainService.getLastMessageNumberOfChat(chatId)
                        .orElse(0L)
        );

        messages.forEach(m ->
                chatMessageDomainService.save(chatMessageMapper.toEntity(chat, m, lastMessageNumber.addAndGet(1))));
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<Message> get(@Nonnull final String conversationId) {

        UUID chatId = UUID.fromString(conversationId);
        ChatEntity chat = chatDomainService.getChatById(chatId);

        return chatMessageDomainService.getAllByChatId(chatId).stream()
                .sorted(Comparator.comparing(ChatMessageEntity::getNumber).reversed())
                .limit(chat.getMaxMessages() > 0 ? chat.getMaxMessages() : maxMessages)
                .map(chatMessageMapper::toMessage)
                .toList();

    }

    @Override
    public void clear(@Nonnull final String conversationId) {
        // NOPE
    }

}

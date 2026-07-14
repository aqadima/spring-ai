package com.example.springai.middleware.service.impl;

import com.example.springai.domain.enums.RoleType;
import com.example.springai.domain.model.ChatEntity;
import com.example.springai.domain.model.ChatMessageEntity;
import com.example.springai.domain.service.ChatDomainService;
import com.example.springai.domain.service.ChatMessageDomainService;
import com.example.springai.middleware.mapper.ChatMapper;
import com.example.springai.middleware.mapper.ChatMessageMapper;
import com.example.springai.middleware.model.request.ChatMessageRequest;
import com.example.springai.middleware.model.request.ChatRequest;
import com.example.springai.middleware.model.response.ChatMessageResponse;
import com.example.springai.middleware.model.response.ChatResponse;
import com.example.springai.middleware.model.response.ChatShortResponse;
import com.example.springai.middleware.service.ChatEdgeService;
import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatFacade implements ChatEdgeService {

    private final ChatDomainService chatDomainService;
    private final ChatMapper chatMapper;
    private final ChatMessageMapper chatMessageMapper;
    private final ChatMessageDomainService chatMessageDomainService;
    private final ChatClient chatClient;

    @Nonnull
    @Override
    public UUID createNewChat(@Nonnull final UUID userId, @Nonnull final ChatRequest chatRequest) {
        ChatEntity newChat = chatDomainService.save(chatMapper.toEntity(chatRequest, userId));
        return newChat.getId();
    }

    @Nonnull
    @Override
    public ChatResponse getChat(@Nonnull final UUID chatId) {
        ChatEntity chat = chatDomainService.getChatById(chatId);
        return chatMapper.toResponse(chat);
    }

    @Nonnull
    @Override
    public List<ChatShortResponse> getAllUserChats(@Nonnull final UUID userId) {
        List<ChatEntity> chats = chatDomainService.getAllUserChats(userId);
        return chatMapper.toShortResponse(chats);
    }

    @Override
    public void deleteChat(UUID chatId) {
        chatDomainService.deleteChat(chatId);
    }

    @Nonnull
    @Override
    public String sendChatMessage(@NonNull UUID chatId, @NonNull String prompt) {

        ChatEntity chat = chatDomainService.getChatById(chatId);
        long lastMessageNumber = chatMessageDomainService.getLastMessageNumberOfChat(chatId)
                .orElse(0L);

        this.addMessage(chat, RoleType.USER, prompt, lastMessageNumber + 1);

        String answer = chatClient.prompt().user(prompt).call().content();
        this.addMessage(chat, RoleType.ASSISTANT, answer, lastMessageNumber + 2);

        return "redirect:/chat/" + chatId;

    }

    private void addMessage(ChatEntity chat, RoleType role, String content, Long messageNumber) {
        chatMessageDomainService.save(
                chatMessageMapper.toEntity(chat, content, role, messageNumber));
    }

}

package com.example.springai.domain.service;

import com.example.springai.domain.model.ChatEntity;

import java.util.List;
import java.util.UUID;

public interface ChatDomainService {

    ChatEntity save(ChatEntity chatEntity);

    ChatEntity getChatById(UUID chatId);

    List<ChatEntity> getAllUserChats(UUID userId);

    void deleteChat(UUID chatId);

}

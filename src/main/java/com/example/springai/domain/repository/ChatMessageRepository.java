package com.example.springai.domain.repository;

import com.example.springai.domain.model.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, UUID> {

    @Query("SELECT cm FROM ChatMessageEntity cm WHERE cm.chat.id = :chatId ORDER BY cm.number ASC")
    List<ChatMessageEntity> findAllByChatId(UUID chatId);

    @Query("SELECT cm.number FROM ChatMessageEntity cm WHERE cm.id = :chatId ORDER BY cm.number DESC LIMIT 1")
    Optional<Long> getLastChatMessageNumberByChatId(UUID chatId);

}

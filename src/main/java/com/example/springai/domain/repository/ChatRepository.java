package com.example.springai.domain.repository;

import com.example.springai.domain.model.ChatEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<ChatEntity, UUID> {

    @Nonnull
    Optional<ChatEntity> findById(@Nonnull final UUID chatId);

    @Nonnull
    @Query("SELECT c FROM ChatEntity c WHERE c.userId = :userId ORDER BY c.updatedAt DESC")
    List<ChatEntity> findAllByUserId(UUID userId);

    void deleteById(@Nonnull final UUID chatId);

}

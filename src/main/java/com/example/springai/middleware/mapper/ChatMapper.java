package com.example.springai.middleware.mapper;

import com.example.springai.domain.model.ChatEntity;
import com.example.springai.middleware.model.request.ChatRequest;
import com.example.springai.middleware.model.response.ChatShortResponse;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.example.springai.middleware.model.response.ChatResponse;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatMapper {

    private final ChatMessageMapper chatMessageMapper;

    @Nonnull
    public ChatResponse toResponse(@Nonnull final ChatEntity chatEntity) {
        return ChatResponse.builder()
                .id(chatEntity.getId())
                .title(chatEntity.getTitle())
                .createdAt(chatEntity.getCreatedAt())
                .updatedAt(chatEntity.getUpdatedAt())
                .messages(chatMessageMapper.toResponse(chatEntity.getMessages()))
                .build();
    }

    @Nonnull
    public List<ChatResponse> toResponse(@Nonnull final List<ChatEntity> targetList) {
        return targetList.stream()
                .map(this::toResponse)
                .toList();
    }

    @Nonnull
    public ChatShortResponse toShortResponse(@Nonnull final ChatEntity chatEntity) {
        return ChatShortResponse.builder()
                .id(chatEntity.getId())
                .title(chatEntity.getTitle())
                .updatedAt(chatEntity.getUpdatedAt())
                .build();
    }

    @Nonnull
    public List<ChatShortResponse> toShortResponse(@Nonnull final List<ChatEntity> targetList) {
        return targetList.stream()
                .map(this::toShortResponse)
                .toList();
    }

    @Nonnull
    public ChatEntity toEntity(@Nonnull final ChatRequest chatRequest, @Nonnull final UUID userId) {
        return ChatEntity.builder()
                .title(chatRequest.title())
                .userId(userId)
                .build();
    }
}

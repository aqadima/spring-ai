package com.example.springai.middleware.mapper;

import com.example.springai.domain.enums.RoleType;
import com.example.springai.domain.model.ChatEntity;
import com.example.springai.domain.model.ChatMessageEntity;
import com.example.springai.middleware.model.request.ChatMessageRequest;
import com.example.springai.middleware.model.response.ChatMessageResponse;
import jakarta.annotation.Nonnull;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatMessageMapper {

    @Nonnull
    public ChatMessageEntity toEntity(@Nonnull final ChatEntity chat,
                                      @Nonnull final Message message,
                                      @Nonnull final Long number
    ) {
        return ChatMessageEntity.builder()
                .chat(chat)
                .userId(chat.getUserId())
                .role(RoleType.getRoleName(message.getMessageType().getValue()))
                .content(message.getText())
                .number(number)
                .build();
    }

    @Nonnull
    public ChatMessageEntity toEntity(@Nonnull final ChatMessageRequest request,
                                      @Nonnull final ChatEntity chat,
                                      @Nonnull final RoleType roleType
    ) {
        return ChatMessageEntity.builder()
                .chat(chat)
                .userId(chat.getUserId())
                .content(request.content())
                .role(roleType)
                .build();
    }

    @Nonnull
    public ChatMessageEntity toEntity(@Nonnull final ChatEntity chat,
                                      @Nonnull final String prompt,
                                      @Nonnull final RoleType roleType,
                                      @Nonnull final Long number
    ) {
        return ChatMessageEntity.builder()
                .chat(chat)
                .userId(chat.getUserId())
                .content(prompt)
                .role(roleType)
                .number(number)
                .build();
    }


    @Nonnull
    public ChatMessageResponse toResponse(@Nonnull final ChatMessageEntity chat) {
        return ChatMessageResponse.builder()
                .id(chat.getId())
                .role(chat.getRole())
                .orderNumber(chat.getNumber())
                .content(chat.getContent())
                .createdAt(chat.getCreatedAt())
                .updatedAt(chat.getUpdatedAt())
                .build();
    }

    @Nonnull
    public List<ChatMessageResponse> toResponse(@Nonnull final List<ChatMessageEntity> chats) {
        return chats.stream()
                .map(this::toResponse)
                .toList();
    }

    @Nonnull
    public Message toMessage(@Nonnull final ChatMessageEntity chatMessage) {
        return chatMessage.getRole().getMessage(chatMessage.getContent());
    }
}

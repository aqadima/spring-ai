package com.example.springai.middleware.model.response;

import com.example.springai.domain.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ChatMessageResponse(

        @JsonProperty("id")
        UUID id,

        @JsonProperty("role")
        RoleType role,

        @JsonProperty("order")
        Long orderNumber,

        @JsonProperty("content")
        String content,

        @JsonProperty("created_at")
        LocalDateTime createdAt,

        @JsonProperty("updated_at")
        LocalDateTime updatedAt

) implements Serializable {
}

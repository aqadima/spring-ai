package com.example.springai.middleware.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

@Builder
public record ChatMessageRequest(

        @NotNull
        @JsonProperty("chat_id")
        UUID chatId,

        @NotBlank
        @JsonProperty("content")
        String content

) implements Serializable {
}

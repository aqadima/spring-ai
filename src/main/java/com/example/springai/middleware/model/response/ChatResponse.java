package com.example.springai.middleware.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
public record ChatResponse(

        @JsonProperty("id")
        UUID id,

        @JsonProperty("title")
        String title,

        @JsonProperty("messages")
        List<ChatMessageResponse> messages,

        @JsonProperty("created_at")
        LocalDateTime createdAt,

        @JsonProperty("updated_at")
        LocalDateTime updatedAt

) implements Serializable {

    public ChatResponse {
        if (messages == null)
            messages = new ArrayList<>();
    }

}

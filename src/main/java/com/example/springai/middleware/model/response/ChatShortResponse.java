package com.example.springai.middleware.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ChatShortResponse(

        @JsonProperty("id")
        UUID id,

        @JsonProperty("title")
        String title,

        @JsonProperty("updated_at")
        LocalDateTime updatedAt

) implements Serializable {
}

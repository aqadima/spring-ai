package com.example.springai.middleware.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record ChatMessageRequest(
        @NotBlank
        @JsonProperty("content")
        String content
) implements Serializable {
}

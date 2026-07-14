package com.example.springai.middleware.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record ChatRequest(

        @JsonProperty("title")
        String title

) implements Serializable {
}

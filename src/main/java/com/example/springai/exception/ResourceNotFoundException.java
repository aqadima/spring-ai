package com.example.springai.exception;

import com.example.springai.domain.enums.ResourceType;
import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private final ResourceType resourceType;
    private final String query;

    public ResourceNotFoundException(ResourceType resourceType, String query) {
        super("Resource not found. Resource type = [%s], query = [%s]".formatted(resourceType, query));
        this.resourceType = resourceType;
        this.query = query;
    }

}

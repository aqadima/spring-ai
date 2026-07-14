package com.example.springai.domain.enums;

import com.example.springai.exception.ResourceNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum ResourceType {

    CHAT("chat"),
    CHAT_MESSAGE("message"),

    USER("user"),

    ROLE_TYPE("role_type"),
    RESOURCE_TYPE("resource_type");

    private final String resourceName;

    public static ResourceType getResourceType(String resourceName) {
        return Stream.of(ResourceType.values())
                .filter(rt -> rt.getResourceName().equalsIgnoreCase(resourceName))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_TYPE, resourceName));
    }

}

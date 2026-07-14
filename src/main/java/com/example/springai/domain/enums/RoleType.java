package com.example.springai.domain.enums;

import com.example.springai.exception.ResourceNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum RoleType {

    USER("user"),
    ASSISTANT("assistant"),
    SYSTEM("system");

    private final String roleName;

    public static RoleType getRoleName(String roleName) {
        return Stream.of(RoleType.values())
                .filter(r -> r.getRoleName().equalsIgnoreCase(roleName))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(ResourceType.ROLE_TYPE, roleName));
    }

}

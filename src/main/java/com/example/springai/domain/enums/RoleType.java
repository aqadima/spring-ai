package com.example.springai.domain.enums;

import com.example.springai.exception.ResourceNotFoundException;
import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum RoleType {

    USER("user") {
        @Nonnull
        @Override
        public UserMessage getMessage(@Nonnull final String prompt) {
            return new UserMessage(prompt);
        }

    },
    ASSISTANT("assistant") {
        @Nonnull
        @Override
        public AssistantMessage getMessage(@Nonnull final String prompt) {
            return new AssistantMessage(prompt);
        }

    },
    SYSTEM("system") {
        @Nonnull
        @Override
        public SystemMessage getMessage(@Nonnull final String prompt) {
            return new SystemMessage(prompt);
        }

    };

    @Nonnull
    private final String roleName;

    @Nonnull
    public static RoleType getRoleName(@Nonnull final String roleName) {
        return Stream.of(RoleType.values())
                .filter(r -> r.getRoleName().equalsIgnoreCase(roleName))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(ResourceType.ROLE_TYPE, roleName));
    }

    @Nonnull
    public abstract Message getMessage(@Nonnull final String prompt);

}

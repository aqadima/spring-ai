package com.example.springai.context;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Getter
@Setter
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
public class UserContextImpl implements UserContext {
    private UUID userId;
    private String username;
}
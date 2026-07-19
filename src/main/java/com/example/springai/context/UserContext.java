package com.example.springai.context;

import java.util.UUID;

public interface UserContext {

    UUID getUserId();

    void setUserId(UUID id);

    String getUsername();

    void setUsername(String username);

}

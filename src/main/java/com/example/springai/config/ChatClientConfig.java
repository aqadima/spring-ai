package com.example.springai.config;

import com.example.springai.domain.repository.ChatRepository;
import com.example.springai.domain.service.ChatDomainService;
import com.example.springai.domain.service.ChatMessageDomainService;
import com.example.springai.middleware.mapper.ChatMessageMapper;
import com.example.springai.middleware.service.PostgresChatMemory;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ChatClientConfig {

    @Value("${app.ai.max-message-history}")
    private int maxMessages;

    @Bean
    public ChatClient chatClient(@Nonnull final ChatClient.Builder builder,
                                 @Nonnull final ChatDomainService chatDomainService,
                                 @Nonnull final ChatMessageDomainService chatMessageDomainService,
                                 @Nonnull final ChatMessageMapper chatMessageMapper
    ) {
        return builder
                .defaultAdvisors(getAdvisor(chatDomainService, chatMessageDomainService, chatMessageMapper))
                .build();
    }


    private Advisor getAdvisor(@Nonnull final ChatDomainService chatDomainService,
                               @Nonnull final ChatMessageDomainService chatMessageDomainService,
                               @Nonnull final ChatMessageMapper chatMessageMapper) {
        return MessageChatMemoryAdvisor.builder(
                        getChatMemory(
                                chatDomainService,
                                chatMessageDomainService,
                                chatMessageMapper
                        ))
                .build();
    }

    private ChatMemory getChatMemory(@Nonnull final ChatDomainService chatDomainService,
                                     @Nonnull final ChatMessageDomainService chatMessageDomainService,
                                     @Nonnull final ChatMessageMapper chatMessageMapper
    ) {
        return PostgresChatMemory.builder()
                .chatDomainService(chatDomainService)
                .chatMessageDomainService(chatMessageDomainService)
                .chatMessageMapper(chatMessageMapper)
                .maxMessages(maxMessages)
                .build();
    }

}

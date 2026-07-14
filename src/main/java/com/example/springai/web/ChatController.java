package com.example.springai.web;

import com.example.springai.middleware.model.request.ChatRequest;
import com.example.springai.middleware.model.response.ChatResponse;
import com.example.springai.middleware.model.response.ChatShortResponse;
import com.example.springai.middleware.service.ChatEdgeService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatController {

    @Value("${app.user.id.default}")
    private UUID defaultUserId;

    private final ChatEdgeService chatEdgeService;

    @PostMapping("/chat/new")
    public String newChat(@RequestParam("title") String title) {
        UUID userId = defaultUserId;
        ChatRequest request = ChatRequest.builder()
                .title(title)
                .build();
        UUID newChatId = chatEdgeService.createNewChat(userId, request);
        return "redirect:/chat/" + newChatId;
    }

    @GetMapping({"/", "/chat", "/chat/"})
    public String mainPage(ModelMap modelMap) {
        modelMap.addAttribute("chats", chatEdgeService.getAllUserChats(defaultUserId));
        return "chat";
    }

    @GetMapping({"/chat/{chat_id}", "/chat/{chat_id}/"})
    public String showChat(@PathVariable("chat_id") final String chatIdText, ModelMap modelMap) {

        UUID chatId = UUID.fromString(chatIdText);
        List<ChatShortResponse> chats = chatEdgeService.getAllUserChats(defaultUserId);
        ChatResponse chat = chatEdgeService.getChat(chatId);

        modelMap.addAttribute("chats", chats);
        modelMap.addAttribute("chat", chat);

        return "chat";

    }

    @PostMapping({"/chat/{chat_id}/message"})
    public String sendMessage(@PathVariable("chat_id") @NotNull final UUID chatId,
                              @RequestParam("prompt") @NotBlank final String prompt
    ) {
        chatEdgeService.sendChatMessage(chatId, prompt);
        return "chat";
    }

    @PostMapping("/chat/{chat_id}/delete")
    public String deleteChat(@PathVariable("chat_id") String chatIdText) {
        UUID chatId = UUID.fromString(chatIdText);
        chatEdgeService.deleteChat(chatId);
        return "chat";
    }

}

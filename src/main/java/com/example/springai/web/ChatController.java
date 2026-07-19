package com.example.springai.web;

import com.example.springai.context.UserContext;
import com.example.springai.middleware.model.request.ChatRequest;
import com.example.springai.middleware.model.response.ChatResponse;
import com.example.springai.middleware.model.response.ChatShortResponse;
import com.example.springai.middleware.service.ChatEdgeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatEdgeService chatEdgeService;
    private final UserContext userContext;

    @Value("${app.user.default.id}")
    private UUID defaultUserId;

    @Value("${app.user.default.username}")
    private String defaultUsername;

    @ModelAttribute
    public void setUserContext() {
        userContext.setUserId(defaultUserId);
        userContext.setUsername(defaultUsername);
    }

    @GetMapping({"/", "/chat", "/chat/"})
    public String mainPage(ModelMap modelMap) {
        modelMap.addAttribute("chats", chatEdgeService.getAllUserActiveChats(defaultUserId));
        return "chat";
    }

    @PostMapping("/chat/new")
    public String newChat(@Valid @NotBlank @RequestParam("title") String title) {
        UUID userId = defaultUserId;
        ChatRequest request = ChatRequest.builder()
                .title(title)
                .build();
        UUID newChatId = chatEdgeService.createNewChat(userId, request);
        return "redirect:/chat/" + newChatId;
    }

    @GetMapping({"/chat/{chat_id}", "/chat/{chat_id}/"})
    public String showChat(@Valid @NotBlank @PathVariable("chat_id") final String chatIdText, ModelMap modelMap) {

        UUID chatId = UUID.fromString(chatIdText);
        List<ChatShortResponse> chats = chatEdgeService.getAllUserActiveChats(defaultUserId);
        ChatResponse chat = chatEdgeService.getChat(chatId);

        modelMap.addAttribute("chats", chats);
        modelMap.addAttribute("chat", chat);

        return "chat";

    }

    @PostMapping("/chat/{chat_id}/archive")
    public String archiveChat(@Valid @NotBlank @PathVariable("chat_id") String chatIdText) {
        UUID chatId = UUID.fromString(chatIdText);
        chatEdgeService.archiveChat(chatId);
        return "redirect:/chat/";
    }

    @PostMapping("/chat/{chat_id}/unarchive")
    public String unarchiveChat(@Valid @NotBlank @PathVariable("chat_id") String chatIdText) {
        UUID chatId = UUID.fromString(chatIdText);
        chatEdgeService.unarchiveChat(chatId);
        return "redirect:/chat/";
    }

    @PostMapping("/chat/{chat_id}/delete")
    public String deleteChat(@Valid @NotBlank @PathVariable("chat_id") String chatIdText) {
        UUID chatId = UUID.fromString(chatIdText);
        chatEdgeService.deleteChat(chatId);
        return "redirect:/chat/";
    }

}

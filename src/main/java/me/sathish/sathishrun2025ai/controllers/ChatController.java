package me.sathish.sathishrun2025ai.controllers;

import me.sathish.sathishrun2025ai.service.SathishChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ChatController {
    final SathishChatService chatService;

    public ChatController(SathishChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chat")
    public String getChatResponse(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return chatService.getAIResponse(message);
    }
}


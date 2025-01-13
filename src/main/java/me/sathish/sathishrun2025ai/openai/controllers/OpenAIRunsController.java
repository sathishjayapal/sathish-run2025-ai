package me.sathish.sathishrun2025ai.openai.controllers;

import me.sathish.sathishrun2025ai.azure.service.AzureIntegratorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class OpenAIRunsController {
    final AzureIntegratorService chatService;

    public OpenAIRunsController(AzureIntegratorService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chat")
    public String getChatResponse(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return chatService.getAIResponse(message);
    }
}

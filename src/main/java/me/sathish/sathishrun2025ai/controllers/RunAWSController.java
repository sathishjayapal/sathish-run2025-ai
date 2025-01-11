package me.sathish.sathishrun2025ai.controllers;

import me.sathish.sathishrun2025ai.data.RunData;
import me.sathish.sathishrun2025ai.service.SathishChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
class RunAWSController {
    final SathishChatService chatService;

    public RunAWSController(SathishChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/topAWSMarathons")
    public RunData getChatResponseForTopMarathons(@RequestParam(value = "year", defaultValue = "2023") String year) {
        return chatService.getAWSAIResponse(year);
    }
}


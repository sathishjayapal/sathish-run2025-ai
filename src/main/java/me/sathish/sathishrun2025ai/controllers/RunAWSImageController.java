package me.sathish.sathishrun2025ai.controllers;

import me.sathish.sathishrun2025ai.service.SathishChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
class RunAWSImageController {
    final SathishChatService chatService;

    public RunAWSImageController(SathishChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/topAWSMarathonImages")
    public Map getChatResponseForTopMarathonImages(@RequestParam(value = "year", defaultValue = "2023") String year) throws IOException {
        return Map.of("embedding", chatService.getAWSImageResponse());

    }
}


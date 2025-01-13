package me.sathish.sathishrun2025ai.azure.controller;

import me.sathish.sathishrun2025ai.data.RunData;
import me.sathish.sathishrun2025ai.azure.service.AzureIntegratorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
class AzureRunsController {
    final AzureIntegratorService chatService;

    public AzureRunsController(AzureIntegratorService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/runners")
    public String getChatResponse(@RequestParam(value = "artist", defaultValue = "Kipchoge") String artist) {
        return chatService.getOpenAIResponseForRunners(artist);
    }

    @GetMapping("/topAzureMarathons")
    public RunData getChatResponseForTopMarathons(@RequestParam(value = "year", defaultValue = "2023") String year) {
        return chatService.getAzureAIResponse(year);
    }
}


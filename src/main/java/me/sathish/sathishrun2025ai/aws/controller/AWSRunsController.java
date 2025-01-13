package me.sathish.sathishrun2025ai.aws.controller;

import me.sathish.sathishrun2025ai.aws.service.AWSIntegratorService;
import me.sathish.sathishrun2025ai.data.RunData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
class AWSRunsController {
    final AWSIntegratorService chatService;

    public AWSRunsController(AWSIntegratorService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/topAWSMarathons")
    public RunData getChatResponseForTopMarathons(@RequestParam(value = "year", defaultValue = "2023") String year) {
        return chatService.getAWSAIResponse(year);
    }
}


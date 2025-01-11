package me.sathish.sathishrun2025ai.controllers;

import me.sathish.sathishrun2025ai.service.RunningRagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RunsQuestionController {
    private final RunningRagService runningRagService;

    public RunsQuestionController(RunningRagService runningRagService) {
        this.runningRagService=runningRagService;

    }

    @GetMapping("/ask")
    public String ask(@RequestParam(value = "question", defaultValue = "What is the weather today") String question) {
        return runningRagService.getRunningRagResponse(question);
    }
}

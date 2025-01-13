package me.sathish.sathishrun2025ai.rag.controller;

import me.sathish.sathishrun2025ai.rag.service.RAGSimpleVectorIntegratorService;
import me.sathish.sathishrun2025ai.rag.service.RunDataMongoIntegratorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RAGRunsController {
    private final RAGSimpleVectorIntegratorService runningRagService;
    private final RunDataMongoIntegratorService assistantService;

    public RAGRunsController(RAGSimpleVectorIntegratorService runningRagService, RunDataMongoIntegratorService assistantService) {
        this.runningRagService = runningRagService;
        this.assistantService = assistantService;
    }

    @GetMapping("/ask")
    public String ask(@RequestParam(value = "question", defaultValue = "What is the weather today") String question) {
        return runningRagService.getRunningRagResponse(question);
    }
    @PostMapping(value = "/public/save-embeddings")
    public ResponseEntity<String> saveEmbeddings(@RequestBody String prompt) {

        String response = assistantService.saveRunData(null, prompt);

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

}

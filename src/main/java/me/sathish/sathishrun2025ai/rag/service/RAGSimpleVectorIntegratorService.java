package me.sathish.sathishrun2025ai.rag.service;

import me.sathish.sathishrun2025ai.all.service.SathishChatConstants;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Scope("prototype")
public class RAGSimpleVectorIntegratorService implements SathishChatConstants {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RAGSimpleVectorIntegratorService.class);
    private final ChatClient chatClient;
    public RAGSimpleVectorIntegratorService(OpenAiChatModel chatModel,
                                            SimpleVectorStore simpleVectorStore) {
        this.chatClient= ChatClient.builder(chatModel).defaultAdvisors
                (new QuestionAnswerAdvisor(simpleVectorStore, SearchRequest.defaults())).build();
        logger.info("AWSIntegratorService Initialized");
    }

    public String getRunningRagResponse(String message) {
        PromptTemplate promptTemplate = new PromptTemplate(message, Map.of("message", message, "format", "json"));
        Prompt prompt= promptTemplate.create();
        ChatResponse chatResponse = chatClient.prompt(prompt).user(message).call().chatResponse();
        assert chatResponse != null;
        return chatResponse.getResult().getOutput().getContent();
    }
}

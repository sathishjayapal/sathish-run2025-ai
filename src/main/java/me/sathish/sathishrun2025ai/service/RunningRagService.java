package me.sathish.sathishrun2025ai.service;

import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.azure.openai.AzureOpenAiChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Scope("prototype")
public class RunningRagService  implements SathishChatConstants{
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RunningRagService.class);
    private final ChatClient chatClient;

    private final OpenAiChatModel chatModel;
    private final Environment environment;
    public RunningRagService(OpenAiChatModel azureOpenAiChatModel,
                             SimpleVectorStore simpleVectorStore, Environment environment) {
        this.chatModel=azureOpenAiChatModel;
//        this.chatModel.getDefaultOptions().setDeploymentName(environment.getProperty(AZURE_DEPLOYMENT_NAME));
        this.environment=environment;
        this.chatClient= ChatClient.builder(chatModel).defaultAdvisors
                (new QuestionAnswerAdvisor(simpleVectorStore, SearchRequest.defaults())).build();
        logger.info("RunningRagService Initialized");
    }

    public String getRunningRagResponse(String message) {
        PromptTemplate promptTemplate = new PromptTemplate(message, Map.of("message", message, "format", "json"));
        Prompt prompt= promptTemplate.create();
        ChatResponse chatResponse = chatClient.prompt(prompt).user(message).call().chatResponse();
        assert chatResponse != null;
        return chatResponse.getResult().getOutput().getContent();
    }
}

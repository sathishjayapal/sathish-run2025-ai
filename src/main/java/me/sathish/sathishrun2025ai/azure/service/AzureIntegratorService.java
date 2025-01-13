package me.sathish.sathishrun2025ai.azure.service;


import me.sathish.sathishrun2025ai.data.RunData;
import me.sathish.sathishrun2025ai.all.service.SathishChatConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.azure.openai.AzureOpenAiChatOptions;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AzureIntegratorService implements SathishChatConstants {
    private static final Logger logger = LoggerFactory.getLogger(AzureIntegratorService.class);
    private final Environment environment;
    private final AzureOpenAiChatModel azureOpenAiChatModel;

    /**
     * Constructor to initialize the chat model and environment
     *
     * @param environment
     */
    public AzureIntegratorService(Environment environment,
                                  AzureOpenAiChatModel azureOpenAiChatModel) {
        logger.info("Chat Service Initialized");
        this.environment = environment;
        this.azureOpenAiChatModel = azureOpenAiChatModel;
    }

    public String getAIResponse(String message) {
        logger.info("Getting AI Response for message: {}", message);
        ChatResponse chatResponse = azureOpenAiChatModel.call(
                new Prompt(
                        message,
                        AzureOpenAiChatOptions.builder()
                                .withDeploymentName(environment.getProperty(AZURE_DEPLOYMENT_NAME)).build()
                ));
        return chatResponse.getResult().getOutput().getContent();
    }

    public String getOpenAIResponseForRunners(String runnerName) {
        logger.info("Getting AI Response for artist: {}", runnerName);
        var message = """
                Please give me a list of three runners in the world and the their best time.
                If you don't know the answer, just say "I don't know" {format} """;
        PromptTemplate promptTemplate = new PromptTemplate(message, Map.of("runnerName", runnerName, "format", "json"));
        Prompt prompt = promptTemplate.create(AzureOpenAiChatOptions.builder()
                .withDeploymentName(environment.getProperty(AZURE_DEPLOYMENT_NAME)).build());
        ChatResponse chatResponse = azureOpenAiChatModel.call(prompt);
        return chatResponse.getResult().getOutput().getContent();
    }


    public RunData getAzureAIResponse(String year) {
        var message = """
                Please give me all the marathons for the year {year}. Also provide the location and the date of the marathon.
                If you don't know the answer, just say "I don't know" {format}""";
        var listOutputParser = new BeanOutputConverter<>(RunData.class);
        PromptTemplate promptTemplate = new PromptTemplate(message, Map.of("year", year, "format", listOutputParser.getFormat()));
        Prompt prompt = promptTemplate.create(AzureOpenAiChatOptions.builder()
                .withDeploymentName(environment.getProperty(AZURE_DEPLOYMENT_NAME)).build());
        ChatResponse chatResponse = azureOpenAiChatModel.call(prompt);
        RunData runData = listOutputParser.convert(chatResponse.getResult().getOutput().getContent());
        return runData;
    }


}

package me.sathish.sathishrun2025ai.service;


import me.sathish.sathishrun2025ai.data.RunData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.azure.openai.AzureOpenAiChatOptions;
import org.springframework.ai.bedrock.jurassic2.BedrockAi21Jurassic2ChatModel;
import org.springframework.ai.bedrock.titan.BedrockTitanChatOptions;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class SathishChatService implements SathishChatConstants {
    private static final Logger logger = LoggerFactory.getLogger(SathishChatService.class);
    private final Environment environment;
    private final BedrockAi21Jurassic2ChatModel bedrockAi21Jurassic2ChatModel;
    private final AzureOpenAiChatModel azureOpenAiChatModel;

    /**
     * Constructor to initialize the chat model and environment
     *
     * @param environment
     */
    public SathishChatService(Environment environment, BedrockAi21Jurassic2ChatModel bedrockAi21Jurassic2ChatModel,
                              AzureOpenAiChatModel azureOpenAiChatModel) {
        logger.info("Chat Service Initialized");
        this.environment = environment;
        this.bedrockAi21Jurassic2ChatModel = bedrockAi21Jurassic2ChatModel;
        this.azureOpenAiChatModel = azureOpenAiChatModel;
    }

    /**
     * Method to get AI response
     *
     * @param message
     * @return
     */
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

    /**
     * JSON Formatted outputI c
     *
     * @param runnerName
     * @return
     */
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

    /**
     * Method to get AI response for top 10 runs of a given year
     *
     * @param year
     * @return
     */
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

    /**
     * Method to get AI response for top 10 runs of a given year
     *
     * @param year
     * @return
     */
    public RunData getAWSAIResponse(String year) {
        var message = """
                Please give me all the marathons for the year {year}. Also provide the location and the date of the marathon.
                If you don't know the answer, just say "I don't know" {format}""";
        var listOutputParser = new BeanOutputConverter<>(RunData.class);
        PromptTemplate promptTemplate = new PromptTemplate(message, Map.of("year", year, "format", listOutputParser.getFormat()));
        Prompt prompt = promptTemplate.create(BedrockTitanChatOptions.builder().build());
        ChatResponse chatResponse = bedrockAi21Jurassic2ChatModel.call(prompt);
        RunData runData = listOutputParser.convert(chatResponse.getResult().getOutput().getContent());
        return runData;
    }

    public EmbeddingResponse getAWSImageResponse() throws IOException {
        var message = """
                blue backpack on a table.
                If you don't know the answer, just say "I don't know" {format}""";
        EmbeddingResponse embeddingResponse = null;
//                embeddingModel.call(
//                new EmbeddingRequest(List.of("Hello World", "World is big and salvation is near"),
//                        BedrockTitanEmbeddingOptions.builder()
//                                .withInputType(BedrockTitanEmbeddingModel.InputType.TEXT)
//                                .build()));
        return embeddingResponse;
    }

}

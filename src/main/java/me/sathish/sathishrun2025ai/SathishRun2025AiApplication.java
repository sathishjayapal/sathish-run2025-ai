package me.sathish.sathishrun2025ai;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.azure.openai.AzureOpenAiChatOptions;
import org.springframework.ai.bedrock.jurassic2.BedrockAi21Jurassic2ChatModel;
import org.springframework.ai.bedrock.titan.BedrockTitanChatOptions;
import org.springframework.ai.bedrock.titan.BedrockTitanEmbeddingModel;
import org.springframework.ai.bedrock.titan.BedrockTitanEmbeddingOptions;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@Slf4j
public class SathishRun2025AiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SathishRun2025AiApplication.class, args);
    }

}

@Service
class SathishChatService {
    private static final Logger logger = LoggerFactory.getLogger(SathishChatService.class);
    public static final String AZURE_DEPLOYMENT_NAME = "AZURE_DEPLOYMENT_NAME";
    private final Environment environment;
    private AzureOpenAiChatModel azureOpenAiChatModel;
    private BedrockAi21Jurassic2ChatModel bedrockAi21Jurassic2ChatModel;
    private BedrockTitanEmbeddingModel embeddingModel;

    /**
     * Constructor to initialize the chat model and environment
     *
     * @param chatModel
     * @param environment
     * @param bedrockAi21Jurassic2ChatModel
     */
    public SathishChatService(AzureOpenAiChatModel chatModel, Environment environment, BedrockAi21Jurassic2ChatModel bedrockAi21Jurassic2ChatModel, BedrockTitanEmbeddingModel embeddingModel) {
        logger.info("Chat Service Initialized");
        this.environment = environment;
        this.azureOpenAiChatModel = chatModel;
        this.embeddingModel = embeddingModel;
        this.bedrockAi21Jurassic2ChatModel =
                bedrockAi21Jurassic2ChatModel;
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
        EmbeddingResponse embeddingResponse = embeddingModel.call(
                new EmbeddingRequest(List.of("Hello World", "World is big and salvation is near"),
                        BedrockTitanEmbeddingOptions.builder()
                                .withInputType(BedrockTitanEmbeddingModel.InputType.TEXT)
                                .build()));
        return embeddingResponse;
    }

}

record RunData(String name, List<CityDate> cityAndDate) {
}

record CityDate(String location, String date) {
}


@RestController
class ChatController {
    final SathishChatService chatService;

    public ChatController(SathishChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chat")
    public String getChatResponse(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return chatService.getAIResponse(message);
    }
}

/**
 *
 */
@RestController
class RunsAzureController {
    final SathishChatService chatService;

    public RunsAzureController(SathishChatService chatService) {
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


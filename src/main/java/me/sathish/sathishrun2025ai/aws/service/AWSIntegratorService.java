package me.sathish.sathishrun2025ai.aws.service;

import me.sathish.sathishrun2025ai.data.RunData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.bedrock.jurassic2.BedrockAi21Jurassic2ChatModel;
import org.springframework.ai.bedrock.titan.BedrockTitanChatOptions;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
@Service
public class AWSIntegratorService {
    private static final Logger logger = LoggerFactory.getLogger(AWSIntegratorService.class);
    public final BedrockAi21Jurassic2ChatModel bedrockAi21Jurassic2ChatModel;

    public AWSIntegratorService(BedrockAi21Jurassic2ChatModel bedrockAi21Jurassic2ChatModel) {
        this.bedrockAi21Jurassic2ChatModel=bedrockAi21Jurassic2ChatModel;
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
        logger.info("Response from AWS AI: {}", runData);
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
        logger.info("Response from AWS AI: {}", embeddingResponse);
        return embeddingResponse;
    }
}

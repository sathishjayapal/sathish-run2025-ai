package me.sathish.sathishrun2025ai.rag.service;

import me.sathish.sathishrun2025ai.data.RunVectorBaseData;
import me.sathish.sathishrun2025ai.rag.repos.MongoRAGRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.ai.bedrock.jurassic2.BedrockAi21Jurassic2ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.awscore.AwsRequestOverrideConfiguration;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

import java.util.ArrayList;
import java.util.List;


@Service
public class RunDataMongoIntegratorService {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RunDataMongoIntegratorService.class);
    private final MongoRAGRepository mongoRAGRepository;
    private BedrockRuntimeClient bedrockRuntimeClient;
    private static final String TITAN = "amazon.titan-text-premier-v1:0";
    public RunDataMongoIntegratorService(MongoRAGRepository mongoRAGRepository) {
        this.mongoRAGRepository = mongoRAGRepository;
        this.bedrockRuntimeClient = BedrockRuntimeClient.builder().build();
        logger.info("RunDataMongoIntegratorService Initialized");
    }

    public String saveRunData(Prompt prompt, String payload) {
        String data = new JSONObject().put("inputText", payload).toString();
        InvokeModelRequest request = InvokeModelRequest.builder().body(SdkBytes.fromUtf8String(data)).modelId(TITAN)
                .contentType("application/json").accept("application/json").build();

        InvokeModelResponse response = bedrockRuntimeClient.invokeModel(request);
        JSONObject responseBody = new JSONObject(response.body().asUtf8String());
        List<Double> vectorData = jsonArrayToList(responseBody.getJSONArray("results"));
        RunVectorBaseData runVectorBaseData = new RunVectorBaseData();
        runVectorBaseData.setRunData(payload);
        runVectorBaseData.setVectorData(vectorData);
        mongoRAGRepository.save(runVectorBaseData);
        return "Run Data Saved";
    }

    private List<Double> jsonArrayToList(JSONArray embedding) {
        List<Double> list = new ArrayList<Double>();

        for (int i = 0; i < embedding.length(); i++) {
            list.add(embedding.getDouble(i));
        }

        return list;
    }


    public RunVectorBaseData getRunData(String id) {
        return mongoRAGRepository.findById(id).orElse(null);
    }

    public void deleteRunData(String id) {
        mongoRAGRepository.deleteById(id);
    }

    public void updateRunData(RunVectorBaseData runVectorBaseData) {
        mongoRAGRepository.save(runVectorBaseData);
    }
}

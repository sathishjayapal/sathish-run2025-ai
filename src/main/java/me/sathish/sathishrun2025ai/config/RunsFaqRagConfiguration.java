package me.sathish.sathishrun2025ai.config;

import lombok.Data;
import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.azure.openai.AzureOpenAiEmbeddingModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.List;

import static me.sathish.sathishrun2025ai.service.SathishChatConstants.AZURE_DEPLOYMENT_NAME;

@Configuration
@Data
public class RunsFaqRagConfiguration {
    @Autowired
    Environment environment;
    @Value("${vectorstore.path}")
    private File vectorStoreName;
    @Value("classpath:/rawdata/running-faqs.txt")
    private Resource runningFaqResource;
    public RunsFaqRagConfiguration() {

    }
    @Bean
    SimpleVectorStore simpleVectorStore(OpenAiEmbeddingModel embeddingModel) {
        var simpleVectorStore = new SimpleVectorStore(embeddingModel);
        System.out.println("VectorStoreName: "+vectorStoreName);
        if(vectorStoreName.exists()) {
            simpleVectorStore.load(vectorStoreName);
        } else {
            TextReader testReader = new TextReader(runningFaqResource);
            testReader.getCustomMetadata().put("filename", runningFaqResource.getFilename());
            List<Document> documentList = testReader.read();
            TextSplitter textSplitter= new TokenTextSplitter();
            List<Document> splitDocuments = textSplitter.apply(documentList);

            simpleVectorStore.add(splitDocuments);
            simpleVectorStore.save(vectorStoreName);
        }
        return simpleVectorStore;
    }
}

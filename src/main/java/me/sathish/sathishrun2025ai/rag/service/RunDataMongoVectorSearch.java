package me.sathish.sathishrun2025ai.rag.service;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.search.FieldSearchPath;
import com.mongodb.client.model.search.SearchPath;
import com.mongodb.client.model.search.VectorSearchOptions;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Aggregates.vectorSearch;
import static com.mongodb.client.model.Projections.*;
import static java.util.Arrays.asList;

@Service
public class RunDataMongoVectorSearch {
    private MongoClient mongoClient;
    @Value("${spring.data.mongodb.dbname}")
    private String appDatabaseName;

    public AggregateIterable<Document> findRunDataVector(List<Double> queryVector) {
        MongoDatabase mongoDatabase = mongoClient.getDatabase(appDatabaseName);
        MongoCollection<Document> collection = mongoDatabase.getCollection("RUN_SEARCH");
        String vectorIndexName = "vector_index";
        FieldSearchPath fieldSearchPath = SearchPath.fieldPath("vector_data");
        int numResults = 5;
        int limit = 1;
        List<Bson> pipeline = asList(vectorSearch(fieldSearchPath, queryVector, vectorIndexName,
                        numResults,
                        VectorSearchOptions.approximateVectorSearchOptions(limit)),
                project(fields(exclude("_id"), include("run_data"),
                        include("active"), metaVectorSearchScore("score"))));
        return collection.aggregate(pipeline);
    }
}

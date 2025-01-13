package me.sathish.sathishrun2025ai.rag.repos;

import me.sathish.sathishrun2025ai.data.RunVectorBaseData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoRAGRepository extends MongoRepository<RunVectorBaseData, String> {

}

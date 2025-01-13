package me.sathish.sathishrun2025ai.data;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "RUN_SEARCH")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class RunVectorBaseData {
    @Id
    private String id;
    @Field("run_data")
    private String runData;
    @Field("vector_data")
    private List<Double> vectorData;

    public String getRunData() {
        return runData;
    }

    public void setRunData(String runData) {
        this.runData = runData;
    }

    public List<Double> getVectorData() {
        return vectorData;
    }

    public void setVectorData(List<Double> vectorData) {
        this.vectorData = vectorData;
    }
}

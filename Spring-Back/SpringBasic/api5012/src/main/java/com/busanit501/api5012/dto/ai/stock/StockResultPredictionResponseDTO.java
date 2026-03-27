package com.busanit501.api5012.dto.ai.stock;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StockResultPredictionResponseDTO {

    @JsonProperty("prediction")  // ✅ Flask 응답의 "prediction" 필드 추가
    private String prediction;

}
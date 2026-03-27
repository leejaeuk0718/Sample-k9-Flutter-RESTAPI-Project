package com.busanit501.api5012.service.ai;

import com.busanit501.api5012.dto.ai.stock.StockPredictionRequestDTO;
import com.busanit501.api5012.dto.ai.stock.StockDataResponseDTO;
import com.busanit501.api5012.dto.ai.stock.StockResultPredictionResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@Log4j2
public class StockPredictionServiceImpl implements StockPredictionService {

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String FLASK_SERVER_URL = "http://43.200.40.31:5000";
//    private final String FLASK_SERVER_URL = "http://localhost:5000";

    private StockResultPredictionResponseDTO sendPredictionRequest(String endpoint, StockPredictionRequestDTO requestDTO) throws IOException, JsonProcessingException {
        // JSON 변환
        String jsonRequest = objectMapper.writeValueAsString(requestDTO);

        RequestBody body = RequestBody.create(jsonRequest, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(FLASK_SERVER_URL + endpoint)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }
            return objectMapper.readValue(response.body().string(), StockResultPredictionResponseDTO.class);
        }
    }

    @Override
    public StockResultPredictionResponseDTO predictWithRNN(StockPredictionRequestDTO requestDTO) throws IOException {
        return sendPredictionRequest("/predict1", requestDTO);
    }

    @Override
    public StockResultPredictionResponseDTO predictWithLSTM(StockPredictionRequestDTO requestDTO) throws IOException {
        return sendPredictionRequest("/predict2", requestDTO);
    }

    @Override
    public StockResultPredictionResponseDTO predictWithGRU(StockPredictionRequestDTO requestDTO) throws IOException {
        return sendPredictionRequest("/predict3", requestDTO);
    }

    @Override
    public List<StockDataResponseDTO> getStockData(String period) throws IOException {
        Request request = new Request.Builder()
                .url(FLASK_SERVER_URL + "/get_stock_data?period=" + period)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }
            return Arrays.asList(objectMapper.readValue(response.body().string(), StockDataResponseDTO[].class));

        }
    }
}

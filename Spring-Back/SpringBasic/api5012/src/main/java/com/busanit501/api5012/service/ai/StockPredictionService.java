package com.busanit501.api5012.service.ai;


import com.busanit501.api5012.dto.ai.stock.StockPredictionRequestDTO;
import com.busanit501.api5012.dto.ai.stock.StockDataResponseDTO;
import com.busanit501.api5012.dto.ai.stock.StockResultPredictionResponseDTO;

import java.io.IOException;
import java.util.List;

public interface StockPredictionService {
    StockResultPredictionResponseDTO predictWithRNN(StockPredictionRequestDTO requestDTO) throws IOException;
    StockResultPredictionResponseDTO predictWithLSTM(StockPredictionRequestDTO requestDTO) throws IOException;
    StockResultPredictionResponseDTO predictWithGRU(StockPredictionRequestDTO requestDTO) throws IOException;
    List<StockDataResponseDTO> getStockData(String period) throws IOException;
}

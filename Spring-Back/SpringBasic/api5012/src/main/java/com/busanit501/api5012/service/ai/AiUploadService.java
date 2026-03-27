package com.busanit501.api5012.service.ai;

import com.busanit501.api5012.dto.ai.image.AiPredictionResponseDTO;
import java.io.IOException;

public interface AiUploadService {
    AiPredictionResponseDTO sendImageToDjangoServer(byte[] imageBytes, String filename,  int teamNo) throws IOException;
}

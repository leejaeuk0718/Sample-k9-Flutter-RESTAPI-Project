package com.busanit501.api5012.dto.library;

import com.busanit501.api5012.domain.library.NoticeImage;
import lombok.*;

/**
 * NoticeImageDTO - 공지사항 첨부 이미지 DTO
 *
 * 공지사항 응답 시 이미지 정보를 전달하는 데이터 전송 객체입니다.
 * NoticeDTO 의 images 리스트에 포함되어 함께 응답됩니다.
 *
 * 클라이언트에서 이미지 URL 구성:
 *   "서버URL" + "/uploads/" + fileName
 *   예: "http://localhost:8080/uploads/550e8400_notice.jpg"
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeImageDTO {

    /** id - 이미지 기본키 */
    private Long id;

    /**
     * fileName - 저장된 이미지 파일명
     * UUID + "_" + 원본파일명 형태입니다.
     * 클라이언트는 서버 URL + "/uploads/" + fileName 으로 이미지를 요청합니다.
     */
    private String fileName;

    /**
     * uuid - UUID 식별자
     * 파일명 고유성 보장에 사용된 UUID 값입니다.
     */
    private String uuid;

    /**
     * ord - 이미지 표시 순서
     * 0부터 시작하며, 낮은 값이 먼저 표시됩니다.
     */
    private int ord;

    // ──────────────────────────────────────────────
    // 정적 팩토리 메서드
    // ──────────────────────────────────────────────

    /**
     * fromEntity - NoticeImage 엔티티를 NoticeImageDTO 로 변환
     *
     * @param image 변환할 NoticeImage 엔티티
     * @return 변환된 NoticeImageDTO
     */
    public static NoticeImageDTO fromEntity(NoticeImage image) {
        return NoticeImageDTO.builder()
                .id(image.getId())
                .fileName(image.getFileName())
                .uuid(image.getUuid())
                .ord(image.getOrd())
                .build();
    }
}

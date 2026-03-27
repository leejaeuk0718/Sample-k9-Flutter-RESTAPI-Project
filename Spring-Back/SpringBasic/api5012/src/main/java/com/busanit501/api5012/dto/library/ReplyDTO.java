package com.busanit501.api5012.dto.library;

import com.busanit501.api5012.domain.library.Reply;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ReplyDTO - 문의사항 답변 DTO
 *
 * 문의사항 답변 조회 응답 및 답변 등록 요청에 사용하는 데이터 전송 객체입니다.
 * InquiryDTO 의 replies 리스트에 포함되어 함께 응답됩니다.
 *
 * API 엔드포인트:
 *   POST /api/library/inquiries/{id}/replies  (답변 등록, 관리자)
 *   PUT  /api/library/replies/{id}            (답변 수정, 관리자)
 *   DELETE /api/library/replies/{id}          (답변 삭제, 관리자)
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {

    /** id - 답변 기본키 */
    private Long id;

    /** replyText - 답변 내용 */
    private String replyText;

    /** replier - 답변 작성자 */
    private String replier;

    /** inquiryId - 소속 문의사항 ID */
    private Long inquiryId;

    /**
     * regDate - 답변 등록일시
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd HH:mm:ss",
                timezone = "Asia/Seoul")
    private LocalDateTime regDate;

    // ──────────────────────────────────────────────
    // 정적 팩토리 메서드
    // ──────────────────────────────────────────────

    /**
     * fromEntity - Reply 엔티티를 ReplyDTO 로 변환
     *
     * @param reply 변환할 Reply 엔티티
     * @return 변환된 ReplyDTO
     */
    public static ReplyDTO fromEntity(Reply reply) {
        return ReplyDTO.builder()
                .id(reply.getId())
                .replyText(reply.getReplyText())
                .replier(reply.getReplier())
                .inquiryId(reply.getInquiry().getId())
                .regDate(reply.getRegDate())
                .build();
    }
}

package com.busanit501.api5012.dto.library;

import com.busanit501.api5012.domain.library.Inquiry;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * InquiryDTO - 문의사항 DTO
 *
 * 문의사항 목록 조회 및 상세 조회 응답에 사용하는 데이터 전송 객체입니다.
 * 답변 목록(List<ReplyDTO>)을 포함합니다.
 *
 * [비밀글 처리 주의]
 * secret = true 인 문의사항은 컨트롤러 레이어에서
 * 작성자 본인 또는 관리자만 content 를 볼 수 있도록 처리해야 합니다.
 * 목록 조회 시에는 content 를 null 또는 "비밀글입니다." 로 대체합니다.
 *
 * API 엔드포인트:
 *   GET    /api/library/inquiries          (문의사항 목록)
 *   GET    /api/library/inquiries/{id}     (문의사항 상세, 답변 포함)
 *   POST   /api/library/inquiries          (문의사항 등록)
 *   PUT    /api/library/inquiries/{id}     (문의사항 수정, 본인만)
 *   DELETE /api/library/inquiries/{id}     (문의사항 삭제, 본인/관리자)
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InquiryDTO {

    /** id - 문의사항 기본키 */
    private Long id;

    /** title - 문의 제목 */
    private String title;

    /**
     * content - 문의 내용
     * 비밀글(secret=true)인 경우 권한 없는 사용자에게는 null 또는 "비밀글입니다." 반환
     */
    private String content;

    /** writer - 작성자 이름 */
    private String writer;

    /** memberId - 작성 회원 ID (권한 확인용) */
    private Long memberId;

    /** answered - 답변 완료 여부 (true: 답변완료, false: 미답변) */
    private boolean answered;

    /** secret - 비밀글 여부 */
    private boolean secret;

    /**
     * regDate - 문의 등록일시
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd HH:mm:ss",
                timezone = "Asia/Seoul")
    private LocalDateTime regDate;

    /**
     * replies - 답변 목록
     * 상세 조회 시에는 전체 답변 목록을 포함합니다.
     * 목록 조회 시에는 빈 리스트로 반환합니다.
     *
     * @Builder.Default : Lombok @Builder 사용 시 기본값 설정 필수
     */
    @Builder.Default
    private List<ReplyDTO> replies = new ArrayList<>();

    // ──────────────────────────────────────────────
    // 정적 팩토리 메서드
    // ──────────────────────────────────────────────

    /**
     * fromEntity - Inquiry 엔티티를 InquiryDTO 로 변환 (답변 포함)
     * replies 컬렉션이 LAZY 로딩이므로 트랜잭션 내에서 호출해야 합니다.
     * 또는 findWithRepliesById() 로 JOIN FETCH 하여 이미 로딩된 상태여야 합니다.
     *
     * @param inquiry 변환할 Inquiry 엔티티
     * @return 답변 목록이 포함된 InquiryDTO
     */
    public static InquiryDTO fromEntity(Inquiry inquiry) {
        return InquiryDTO.builder()
                .id(inquiry.getId())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .writer(inquiry.getWriter())
                .memberId(inquiry.getMember() != null ? inquiry.getMember().getId() : null)
                .answered(inquiry.isAnswered())
                .secret(inquiry.isSecret())
                .regDate(inquiry.getRegDate())
                .replies(inquiry.getReplies().stream()
                        .map(ReplyDTO::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * fromEntityForList - 목록 조회용 변환 (답변 제외, 비밀글 내용 마스킹)
     * 문의사항 목록 화면에서 content 는 표시하지 않으므로 제외합니다.
     * 비밀글은 제목도 "비밀글입니다." 로 마스킹합니다.
     *
     * @param inquiry     변환할 Inquiry 엔티티
     * @param isOwnerOrAdmin 작성자 또는 관리자 여부 (true 이면 마스킹 없이 반환)
     * @return 목록용 InquiryDTO
     */
    public static InquiryDTO fromEntityForList(Inquiry inquiry, boolean isOwnerOrAdmin) {
        String displayTitle = (inquiry.isSecret() && !isOwnerOrAdmin)
                ? "비밀글입니다."
                : inquiry.getTitle();

        return InquiryDTO.builder()
                .id(inquiry.getId())
                .title(displayTitle)
                .writer(inquiry.getWriter())
                .memberId(inquiry.getMember() != null ? inquiry.getMember().getId() : null)
                .answered(inquiry.isAnswered())
                .secret(inquiry.isSecret())
                .regDate(inquiry.getRegDate())
                .build(); // content 와 replies 는 포함하지 않음
    }
}

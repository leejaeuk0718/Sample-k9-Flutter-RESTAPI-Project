package com.busanit501.api5012.dto.library;

import com.busanit501.api5012.domain.library.EventApplication;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * EventApplicationDTO - 행사 참가 신청 DTO
 *
 * 행사 신청 내역 조회 응답에 사용하는 데이터 전송 객체입니다.
 * 연관 엔티티(LibraryEvent, Member)의 주요 정보를 평탄화하여 포함합니다.
 *
 * API 엔드포인트:
 *   GET  /api/library/event-applications/my        (내 신청 이력, 회원용)
 *   GET  /api/library/event-applications/{eventId} (행사별 신청자 목록, 관리자용)
 *   POST /api/library/event-applications           (행사 신청)
 *   PUT  /api/library/event-applications/{id}/cancel (신청 취소)
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventApplicationDTO {

    /** id - 신청 기본키 */
    private Long id;

    /** eventId - 신청한 행사 ID */
    private Long eventId;

    /** eventTitle - 행사 제목 (평탄화) */
    private String eventTitle;

    /** eventCategory - 행사 카테고리 (평탄화) */
    private String eventCategory;

    /**
     * eventDate - 행사 개최일 (평탄화)
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate eventDate;

    /** eventPlace - 행사 장소 (평탄화) */
    private String eventPlace;

    /** memberId - 신청 회원 ID */
    private Long memberId;

    /** memberName - 신청 회원 이름 (평탄화) */
    private String memberName;

    /**
     * applyDate - 신청일시
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd HH:mm:ss",
                timezone = "Asia/Seoul")
    private LocalDateTime applyDate;

    /** status - 신청 상태 ("APPLIED" / "CANCELLED") */
    private String status;

    // ──────────────────────────────────────────────
    // 정적 팩토리 메서드
    // ──────────────────────────────────────────────

    /**
     * fromEntity - EventApplication 엔티티를 EventApplicationDTO 로 변환
     * event 와 member 가 LAZY 로딩이므로 트랜잭션 내에서 호출해야 합니다.
     *
     * @param application 변환할 EventApplication 엔티티
     * @return 변환된 EventApplicationDTO
     */
    public static EventApplicationDTO fromEntity(EventApplication application) {
        return EventApplicationDTO.builder()
                .id(application.getId())
                .eventId(application.getEvent().getId())
                .eventTitle(application.getEvent().getTitle())
                .eventCategory(application.getEvent().getCategory())
                .eventDate(application.getEvent().getEventDate())
                .eventPlace(application.getEvent().getPlace())
                .memberId(application.getMember().getId())
                .memberName(application.getMember().getMname())
                .applyDate(application.getApplyDate())
                .status(application.getStatus())
                .build();
    }
}

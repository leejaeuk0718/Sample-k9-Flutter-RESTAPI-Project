package com.busanit501.api5012.dto.library;

import com.busanit501.api5012.domain.library.LibraryEvent;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * LibraryEventDTO - 도서관 행사 DTO
 *
 * 행사 목록 조회, 상세 조회, 등록/수정 요청에 사용하는 데이터 전송 객체입니다.
 * 잔여 신청 가능 인원(remainingSlots)을 계산하여 클라이언트에 제공합니다.
 *
 * API 엔드포인트:
 *   GET  /api/library/events           (행사 목록)
 *   GET  /api/library/events/{id}      (행사 상세)
 *   POST /api/library/events           (행사 등록, 관리자)
 *   PUT  /api/library/events/{id}      (행사 수정, 관리자)
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LibraryEventDTO {

    /** id - 행사 기본키 */
    private Long id;

    /** category - 행사 카테고리 ("문화행사", "주말극장", "강좌") */
    private String category;

    /** title - 행사 제목 */
    private String title;

    /** content - 행사 상세 내용 */
    private String content;

    /**
     * eventDate - 행사 개최일
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate eventDate;

    /** place - 행사 장소 */
    private String place;

    /** maxParticipants - 최대 참가 인원 */
    private int maxParticipants;

    /** currentParticipants - 현재 신청 인원 */
    private int currentParticipants;

    /**
     * remainingSlots - 잔여 신청 가능 인원 (편의 필드)
     * maxParticipants - currentParticipants 로 계산합니다.
     * 클라이언트에서 계산 없이 바로 표시할 수 있습니다.
     */
    private int remainingSlots;

    /** status - 행사 신청 상태 ("OPEN" / "CLOSED") */
    private String status;

    /**
     * regDate - 행사 등록일시
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd HH:mm:ss",
                timezone = "Asia/Seoul")
    private LocalDateTime regDate;

    // ──────────────────────────────────────────────
    // 정적 팩토리 메서드
    // ──────────────────────────────────────────────

    /**
     * fromEntity - LibraryEvent 엔티티를 LibraryEventDTO 로 변환
     * remainingSlots 는 maxParticipants - currentParticipants 로 계산합니다.
     *
     * @param event 변환할 LibraryEvent 엔티티
     * @return 변환된 LibraryEventDTO
     */
    public static LibraryEventDTO fromEntity(LibraryEvent event) {
        return LibraryEventDTO.builder()
                .id(event.getId())
                .category(event.getCategory())
                .title(event.getTitle())
                .content(event.getContent())
                .eventDate(event.getEventDate())
                .place(event.getPlace())
                .maxParticipants(event.getMaxParticipants())
                .currentParticipants(event.getCurrentParticipants())
                .remainingSlots(event.getMaxParticipants() - event.getCurrentParticipants())
                .status(event.getStatus())
                .regDate(event.getRegDate())
                .build();
    }
}

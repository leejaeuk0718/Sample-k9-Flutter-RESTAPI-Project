package com.busanit501.api5012.dto.library;

import com.busanit501.api5012.domain.library.Apply;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ApplyDTO - 시설 예약 신청 DTO
 *
 * 시설 예약 신청 내역 조회 및 신청 요청에 사용하는 데이터 전송 객체입니다.
 *
 * API 엔드포인트:
 *   GET  /api/library/applies/my        (내 예약 신청 목록, 회원용)
 *   GET  /api/library/applies           (전체 예약 목록, 관리자용)
 *   POST /api/library/applies           (예약 신청)
 *   PUT  /api/library/applies/{id}/approve (승인, 관리자)
 *   PUT  /api/library/applies/{id}/reject  (반려, 관리자)
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplyDTO {

    /** id - 예약 신청 기본키 */
    private Long id;

    /** memberId - 신청 회원 ID */
    private Long memberId;

    /** memberName - 신청 회원 이름 (평탄화) */
    private String memberName;

    /** applicantName - 실제 이용 담당자 이름 */
    private String applicantName;

    /**
     * facilityType - 시설 유형
     * "세미나실", "스터디룸", "강당" 중 하나
     */
    private String facilityType;

    /** phone - 연락처 */
    private String phone;

    /** participants - 이용 예정 인원 */
    private int participants;

    /**
     * reserveDate - 예약 희망일
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate reserveDate;

    /**
     * status - 처리 상태
     * "PENDING"(대기), "APPROVED"(승인), "REJECTED"(반려)
     */
    private String status;

    /**
     * regDate - 신청 등록일시
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd HH:mm:ss",
                timezone = "Asia/Seoul")
    private LocalDateTime regDate;

    // ──────────────────────────────────────────────
    // 정적 팩토리 메서드
    // ──────────────────────────────────────────────

    /**
     * fromEntity - Apply 엔티티를 ApplyDTO 로 변환
     *
     * @param apply 변환할 Apply 엔티티
     * @return 변환된 ApplyDTO
     */
    public static ApplyDTO fromEntity(Apply apply) {
        return ApplyDTO.builder()
                .id(apply.getId())
                .memberId(apply.getMember().getId())
                .memberName(apply.getMember().getMname())
                .applicantName(apply.getApplicantName())
                .facilityType(apply.getFacilityType())
                .phone(apply.getPhone())
                .participants(apply.getParticipants())
                .reserveDate(apply.getReserveDate())
                .status(apply.getStatus())
                .regDate(apply.getRegDate())
                .build();
    }
}

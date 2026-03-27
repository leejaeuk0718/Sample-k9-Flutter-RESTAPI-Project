package com.busanit501.api5012.domain.library;

/**
 * RentalStatus - 대여 상태 열거형
 *
 * 도서 대여 기록(Rental)의 현재 상태를 나타냅니다.
 * 대여 처리, 반납 처리, 연체 관리 등의 비즈니스 로직에서 사용됩니다.
 *
 * 상태 전이 흐름:
 *   RENTING(대여중) → RETURNED(반납완료)
 *   RENTING(대여중) → OVERDUE(연체)   [반납일 초과 시]
 *   RENTING(대여중) → EXTENDED(연장)  [연장 신청 시]
 *   EXTENDED(연장)  → RETURNED(반납완료)
 *   EXTENDED(연장)  → OVERDUE(연체)   [연장 후 반납일 초과 시]
 */
public enum RentalStatus {

    /**
     * RENTING - 대여중
     * 현재 도서를 대여하고 있는 상태입니다.
     * 대여 등록 시 기본값(default)으로 설정됩니다.
     */
    RENTING,

    /**
     * RETURNED - 반납완료
     * 대여한 도서를 정상적으로 반납한 상태입니다.
     * 반납 처리 시 RENTING 또는 EXTENDED → RETURNED 로 변경됩니다.
     */
    RETURNED,

    /**
     * OVERDUE - 연체
     * 반납 기한(dueDate)을 초과하였으나 아직 반납하지 않은 상태입니다.
     * 스케줄러 등으로 자동 감지하거나 관리자가 수동으로 처리합니다.
     */
    OVERDUE,

    /**
     * EXTENDED - 연장
     * 대여 기간을 연장한 상태입니다.
     * 연장 신청 승인 시 RENTING → EXTENDED 로 변경되고
     * dueDate 가 새로운 반납 기한으로 업데이트됩니다.
     */
    EXTENDED
}

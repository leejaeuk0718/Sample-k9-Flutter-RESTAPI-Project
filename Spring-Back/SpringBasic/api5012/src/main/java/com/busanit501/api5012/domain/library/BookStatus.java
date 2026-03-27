package com.busanit501.api5012.domain.library;

/**
 * BookStatus - 도서 상태 열거형
 *
 * 도서관 소장 도서의 현재 상태를 나타냅니다.
 * Book 엔티티의 status 필드에서 사용되며,
 * 대여 가능 여부 확인 및 도서 목록 필터링에 활용됩니다.
 */
public enum BookStatus {

    /**
     * AVAILABLE - 대여가능
     * 도서가 도서관에 반납되어 있어 즉시 대여할 수 있는 상태입니다.
     * 도서 등록 시 기본값(default)으로 설정됩니다.
     */
    AVAILABLE,

    /**
     * RENTED - 대여중
     * 현재 다른 회원이 대여하고 있는 상태입니다.
     * 대여 처리 시 AVAILABLE → RENTED 로 변경됩니다.
     */
    RENTED,

    /**
     * RESERVED - 예약중
     * 다른 회원이 대여 중인 도서를 다음 대여자가 예약한 상태입니다.
     * 예약 기능 구현 시 활용됩니다.
     */
    RESERVED,

    /**
     * LOST - 분실
     * 도서가 분실되어 대여/예약이 불가능한 상태입니다.
     * 관리자가 분실 처리한 도서에 설정됩니다.
     */
    LOST
}

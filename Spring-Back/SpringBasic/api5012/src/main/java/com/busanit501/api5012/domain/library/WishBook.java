package com.busanit501.api5012.domain.library;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * WishBook - 희망 도서 신청 엔티티
 *
 * 회원이 도서관에 구입을 희망하는 도서를 신청하는 기록을 저장하는 JPA 엔티티입니다.
 * 도서관 측에서 신청된 도서를 검토하여 구입 여부를 결정합니다.
 *
 * 처리 상태 흐름:
 *   REQUESTED(신청중) → APPROVED(구입결정) : 도서 구입 확정
 *   REQUESTED(신청중) → REJECTED(반려)     : 구입 불가 (절판, 예산 부족 등)
 *
 * DB 테이블명: tbl_lib_wish_book
 */
@Entity
@Table(name = "tbl_lib_wish_book")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "member")
public class WishBook {

    /**
     * id - 기본키 (Primary Key)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * member - 신청 회원 (다대일 연관관계)
     * 희망 도서를 신청한 회원 정보를 참조합니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /**
     * wishBookTitle - 희망 도서 제목
     * 구입을 희망하는 도서의 제목입니다.
     */
    @Column(nullable = false, length = 300)
    private String wishBookTitle;

    /**
     * wishAuthor - 희망 도서 저자
     * 구입을 희망하는 도서의 저자명입니다.
     * 동명이인의 저자가 있을 수 있으므로 저자 정보를 함께 기재합니다.
     */
    @Column(length = 100)
    private String wishAuthor;

    /**
     * wishPublisher - 희망 도서 출판사
     * 구입을 희망하는 도서의 출판사명입니다.
     * 도서 식별에 도움이 됩니다.
     */
    @Column(length = 100)
    private String wishPublisher;

    /**
     * reason - 신청 사유
     * 해당 도서를 도서관에서 구입해야 하는 이유를 기술합니다.
     * 예: "컴퓨터 프로그래밍 학습에 필요한 도서입니다."
     */
    @Column(length = 500)
    private String reason;

    /**
     * status - 처리 상태
     * - "REQUESTED" : 신청중 (기본값, 처리 대기)
     * - "APPROVED"  : 구입 결정 (도서관 측 승인)
     * - "REJECTED"  : 반려 (절판, 예산 초과 등의 사유로 구입 불가)
     * 향후 Enum 으로 리팩토링을 고려할 수 있습니다.
     */
    @Column(nullable = false, length = 15)
    @Builder.Default
    private String status = "REQUESTED";

    /**
     * regDate - 신청 등록일시
     * 희망 도서 신청이 등록된 날짜와 시간입니다.
     */
    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime regDate = LocalDateTime.now();

    // ──────────────────────────────────────────────
    // 비즈니스 메서드 (도메인 로직)
    // ──────────────────────────────────────────────

    /**
     * approve - 구입 승인 처리 메서드
     * 도서관 담당자가 해당 도서 구입을 결정할 때 호출합니다.
     */
    public void approve() {
        this.status = "APPROVED";
    }

    /**
     * reject - 구입 반려 처리 메서드
     * 도서관 담당자가 구입 불가를 결정할 때 호출합니다.
     */
    public void reject() {
        this.status = "REJECTED";
    }

    /**
     * isPending - 처리 대기 여부 확인 메서드
     *
     * @return 아직 처리되지 않은 신청이면 true
     */
    public boolean isPending() {
        return "REQUESTED".equals(this.status);
    }
}

package com.busanit501.api5012.domain.library;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Rental - 도서 대여 기록 엔티티
 *
 * 회원이 도서를 대여한 기록을 저장하는 JPA 엔티티입니다.
 * 대여 처리, 반납 처리, 연체 관리, 연장 처리의 핵심 테이블입니다.
 *
 * 연관관계:
 *   - Member (N:1) : 대여한 회원 (LAZY 로딩 - 필요할 때만 DB 조회)
 *   - Book   (N:1) : 대여된 도서 (LAZY 로딩 - 필요할 때만 DB 조회)
 *
 * DB 테이블명: tbl_lib_rental
 *
 * [LAZY vs EAGER 로딩 설명]
 * - LAZY  : 연관 엔티티를 실제로 사용할 때 SELECT 쿼리 실행 (권장)
 * - EAGER : 엔티티 로딩 시 연관 엔티티도 함께 즉시 로딩 (성능 저하 위험)
 */
@Entity
@Table(name = "tbl_lib_rental")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"member", "book"}) // 순환 참조(StackOverflow) 방지
public class Rental {

    /**
     * id - 기본키 (Primary Key)
     * DB에서 자동으로 증가하는 숫자형 식별자입니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * member - 대여 회원 (다대일 연관관계)
     * 여러 대여 기록이 하나의 회원에 속합니다. (N:1)
     * fetch = FetchType.LAZY : 대여 기록 조회 시 회원 정보는 즉시 로딩하지 않고,
     *                          member 필드에 실제로 접근할 때 DB를 조회합니다.
     * @JoinColumn : DB에 member_id 외래키 컬럼이 생성됩니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /**
     * book - 대여 도서 (다대일 연관관계)
     * 여러 대여 기록이 하나의 도서에 속합니다. (N:1)
     * 동일한 도서가 반납 후 다시 대여될 수 있으므로 같은 도서의 기록이 여러 개일 수 있습니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    /**
     * rentalDate - 대여일
     * 도서를 실제로 대여한 날짜입니다.
     * 대여 등록 시 현재 날짜로 자동 설정합니다.
     */
    @Column(nullable = false)
    private LocalDate rentalDate;

    /**
     * dueDate - 반납 기한일
     * 도서를 반납해야 하는 마감 날짜입니다.
     * 일반적으로 대여일로부터 14일(2주) 후로 설정합니다.
     * 연장 시 이 날짜가 업데이트됩니다.
     */
    @Column(nullable = false)
    private LocalDate dueDate;

    /**
     * returnDate - 실제 반납일
     * 도서를 실제로 반납한 날짜입니다.
     * 대여 중일 때는 null 이며, 반납 처리 시 현재 날짜로 설정됩니다.
     * nullable = true 가 기본값이므로 별도 설정 없이 null 허용됩니다.
     */
    private LocalDate returnDate;

    /**
     * status - 대여 상태
     * RentalStatus 열거형으로 현재 대여 기록의 상태를 나타냅니다.
     * - RENTING  : 대여중 (기본값)
     * - RETURNED : 반납완료
     * - OVERDUE  : 연체
     * - EXTENDED : 연장
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    @Builder.Default
    private RentalStatus status = RentalStatus.RENTING;

    // ──────────────────────────────────────────────
    // 비즈니스 메서드 (도메인 로직)
    // ──────────────────────────────────────────────

    /**
     * processReturn - 반납 처리 메서드
     * 실제 반납일을 기록하고 상태를 RETURNED 로 변경합니다.
     * 서비스 레이어에서 도서 상태(BookStatus.AVAILABLE)도 함께 변경해야 합니다.
     *
     * @param returnDate 실제 반납일 (보통 오늘 날짜)
     */
    public void processReturn(LocalDate returnDate) {
        this.returnDate = returnDate;
        this.status = RentalStatus.RETURNED;
    }

    /**
     * extendDueDate - 반납 기한 연장 메서드
     * 연장 신청 승인 시 새로운 반납 기한을 설정하고 상태를 EXTENDED 로 변경합니다.
     *
     * @param newDueDate 연장된 새 반납 기한
     */
    public void extendDueDate(LocalDate newDueDate) {
        this.dueDate = newDueDate;
        this.status = RentalStatus.EXTENDED;
    }

    /**
     * markOverdue - 연체 처리 메서드
     * 반납 기한을 초과한 경우 상태를 OVERDUE 로 변경합니다.
     * 스케줄러(@Scheduled)에서 매일 자정에 자동으로 호출할 수 있습니다.
     */
    public void markOverdue() {
        this.status = RentalStatus.OVERDUE;
    }

    /**
     * isOverdue - 연체 여부 확인 메서드
     * 현재 날짜가 반납 기한을 초과했는지 확인합니다.
     *
     * @return 연체이면 true, 정상이면 false
     */
    public boolean isOverdue() {
        return LocalDate.now().isAfter(this.dueDate)
                && this.status != RentalStatus.RETURNED;
    }
}

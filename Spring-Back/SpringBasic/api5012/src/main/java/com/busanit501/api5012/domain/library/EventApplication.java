package com.busanit501.api5012.domain.library;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * EventApplication - 행사 참가 신청 엔티티
 *
 * 회원이 도서관 행사에 참가 신청한 기록을 저장하는 JPA 엔티티입니다.
 * 한 회원이 같은 행사에 중복 신청하지 못하도록 서비스 레이어에서 검증해야 합니다.
 *
 * 연관관계:
 *   - LibraryEvent (N:1) : 신청한 행사 (LAZY 로딩)
 *   - Member       (N:1) : 신청한 회원 (LAZY 로딩)
 *
 * DB 테이블명: tbl_lib_event_application
 */
@Entity
@Table(name = "tbl_lib_event_application")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"event", "member"}) // 연관 엔티티의 순환 참조 방지
public class EventApplication {

    /**
     * id - 기본키 (Primary Key)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * event - 신청한 행사 (다대일 연관관계)
     * 여러 신청 기록이 하나의 행사에 속합니다. (N:1)
     * LAZY 로딩으로 필요 시에만 행사 정보를 DB 에서 가져옵니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private LibraryEvent event;

    /**
     * member - 신청한 회원 (다대일 연관관계)
     * 여러 신청 기록이 하나의 회원에 속합니다. (N:1)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /**
     * applyDate - 신청일시
     * 행사 신청이 완료된 날짜와 시간입니다.
     * 기본값으로 현재 시각이 자동 설정됩니다.
     */
    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime applyDate = LocalDateTime.now();

    /**
     * status - 신청 상태
     * - "APPLIED"    : 신청완료 (기본값)
     * - "CANCELLED"  : 신청취소
     * 향후 Enum 으로 리팩토링을 고려할 수 있습니다.
     */
    @Column(nullable = false, length = 15)
    @Builder.Default
    private String status = "APPLIED";

    // ──────────────────────────────────────────────
    // 비즈니스 메서드
    // ──────────────────────────────────────────────

    /**
     * cancel - 행사 신청 취소 메서드
     * 신청 상태를 CANCELLED 로 변경합니다.
     * 서비스 레이어에서 LibraryEvent.decreaseParticipants() 도 함께 호출해야 합니다.
     */
    public void cancel() {
        this.status = "CANCELLED";
    }

    /**
     * isActive - 유효한 신청 여부 확인 메서드
     * 취소되지 않은 활성 신청인지 확인합니다.
     *
     * @return 신청이 유효하면 true, 취소되었으면 false
     */
    public boolean isActive() {
        return "APPLIED".equals(this.status);
    }
}

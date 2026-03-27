package com.busanit501.api5012.domain.library;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Reply - 문의사항 답변 엔티티
 *
 * 회원의 문의사항(Inquiry)에 대한 관리자 답변을 저장하는 JPA 엔티티입니다.
 * Inquiry 와 1:N 관계이며, Inquiry 가 삭제되면 답변도 함께 삭제됩니다.
 *
 * 연관관계:
 *   - Inquiry (N:1) : 이 답변이 속한 문의사항 (LAZY 로딩)
 *
 * DB 테이블명: tbl_lib_reply
 *
 * [설계 참고]
 * Reply 를 Inquiry 보다 먼저 정의하면 Inquiry 에서 List<Reply> 참조 시
 * 컴파일 오류가 발생하지 않습니다. 여기서는 두 파일을 함께 생성하므로 무관합니다.
 */
@Entity
@Table(name = "tbl_lib_reply")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "inquiry") // 순환 참조 방지
public class Reply {

    /**
     * id - 기본키 (Primary Key)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * replyText - 답변 내용
     * 관리자가 작성한 문의 답변 내용입니다.
     * @Lob 으로 선언하여 긴 답변도 저장할 수 있습니다.
     */
    @Lob
    @Column(nullable = false)
    private String replyText;

    /**
     * replier - 답변 작성자
     * 답변을 작성한 관리자의 이름 또는 아이디입니다.
     * 예: "도서관 담당자", "admin"
     */
    @Column(nullable = false, length = 100)
    private String replier;

    /**
     * inquiry - 소속 문의사항 (다대일 연관관계)
     * 이 답변이 속한 문의사항을 참조합니다.
     * LAZY 로딩으로 불필요한 문의사항 데이터 조회를 방지합니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_id", nullable = false)
    private Inquiry inquiry;

    /**
     * regDate - 답변 등록일시
     * 답변이 작성된 날짜와 시간입니다.
     */
    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime regDate = LocalDateTime.now();

    // ──────────────────────────────────────────────
    // 비즈니스 메서드
    // ──────────────────────────────────────────────

    /**
     * changeReplyText - 답변 내용 수정 메서드
     * 등록된 답변의 내용을 수정할 때 사용합니다.
     *
     * @param replyText 수정할 새 답변 내용
     */
    public void changeReplyText(String replyText) {
        this.replyText = replyText;
    }

    /**
     * setInquiry - 문의사항 연관관계 설정 메서드
     * Inquiry 엔티티에서 답변을 추가할 때 양방향 관계를 설정하기 위해 사용합니다.
     *
     * @param inquiry 이 답변이 속할 문의사항
     */
    public void setInquiry(Inquiry inquiry) {
        this.inquiry = inquiry;
    }
}

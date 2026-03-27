package com.busanit501.api5012.domain.library;

import jakarta.persistence.*;
import lombok.*;

/**
 * NoticeImage - 공지사항 첨부 이미지 엔티티
 *
 * 공지사항(Notice)에 첨부된 이미지 파일 정보를 저장합니다.
 * Notice 와 1:N 관계이며, Notice 가 삭제되면 이미지도 함께 삭제됩니다.
 * (cascade = ALL, orphanRemoval = true 설정으로 자동 처리)
 *
 * 파일 저장 방식:
 *   - 실제 파일은 서버의 업로드 디렉토리에 저장됩니다.
 *   - DB에는 UUID + 원본파일명 조합의 fileName 만 저장합니다.
 *   - UUID 를 파일명 앞에 붙이면 동일한 파일명 충돌을 방지할 수 있습니다.
 *
 * DB 테이블명: tbl_lib_notice_image
 */
@Entity
@Table(name = "tbl_lib_notice_image")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "notice") // 순환 참조 방지
public class NoticeImage {

    /**
     * id - 기본키 (Primary Key)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * fileName - 저장된 이미지 파일명
     * 서버 업로드 디렉토리에 저장된 실제 파일명입니다.
     * uuid + "_" + 원본파일명 형태로 구성하는 것을 권장합니다.
     * 예: "550e8400-e29b-41d4-a716-446655440000_notice.jpg"
     */
    @Column(nullable = false, length = 300)
    private String fileName;

    /**
     * uuid - UUID (Universally Unique Identifier)
     * 파일명 중복을 방지하기 위해 생성된 고유 식별자입니다.
     * Java의 UUID.randomUUID().toString() 으로 생성합니다.
     * 예: "550e8400-e29b-41d4-a716-446655440000"
     */
    @Column(nullable = false, length = 100)
    private String uuid;

    /**
     * ord - 이미지 순서 (order)
     * 공지사항에 여러 이미지가 첨부된 경우 표시 순서를 나타냅니다.
     * 0부터 시작하며, 숫자가 낮을수록 먼저 표시됩니다.
     * 'order' 는 SQL 예약어이므로 'ord' 로 대체합니다.
     */
    @Column(nullable = false)
    private int ord;

    /**
     * notice - 소속 공지사항 (다대일 연관관계)
     * 이 이미지가 속한 공지사항을 참조합니다.
     * LAZY 로딩으로 불필요한 공지사항 데이터 조회를 방지합니다.
     * @JoinColumn : DB에 notice_id 외래키 컬럼이 생성됩니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id", nullable = false)
    private Notice notice;

    // ──────────────────────────────────────────────
    // 비즈니스 메서드
    // ──────────────────────────────────────────────

    /**
     * changeOrd - 이미지 순서 변경 메서드
     * 이미지 순서를 재정렬할 때 사용합니다.
     *
     * @param ord 새로운 순서 번호
     */
    public void changeOrd(int ord) {
        this.ord = ord;
    }

    /**
     * setNotice - 공지사항 연관관계 설정 메서드
     * Notice 엔티티에서 이미지를 추가할 때 양방향 관계를 설정하기 위해 사용합니다.
     * 패키지 내부에서만 사용하도록 접근 제어자를 고려할 수 있습니다.
     *
     * @param notice 이 이미지가 속할 공지사항
     */
    public void setNotice(Notice notice) {
        this.notice = notice;
    }
}

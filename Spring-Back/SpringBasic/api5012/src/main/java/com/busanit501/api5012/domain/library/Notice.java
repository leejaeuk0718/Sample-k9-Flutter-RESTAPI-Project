package com.busanit501.api5012.domain.library;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Notice - 공지사항 엔티티
 *
 * 도서관 공지사항 정보를 저장하는 JPA 엔티티입니다.
 * 이미지 첨부 기능과 상단 고정 기능을 지원합니다.
 *
 * 연관관계:
 *   - NoticeImage (1:N) : 공지사항에 첨부된 이미지 목록
 *     cascade = ALL    : Notice 저장/수정/삭제 시 이미지도 함께 처리
 *     orphanRemoval    : Notice 에서 이미지 제거 시 DB에서도 자동 삭제
 *
 * DB 테이블명: tbl_lib_notice
 */
@Entity
@Table(name = "tbl_lib_notice")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "images") // 컬렉션 필드는 toString 에서 제외 (성능 문제 방지)
public class Notice {

    /**
     * id - 기본키 (Primary Key)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * title - 공지사항 제목
     * 목록 화면에서 표시되는 제목입니다.
     */
    @Column(nullable = false, length = 300)
    private String title;

    /**
     * content - 공지사항 내용
     * 내용이 길 수 있으므로 @Lob (CLOB)으로 선언합니다.
     * HTML 태그를 포함한 Rich Text 형태로 저장할 수 있습니다.
     */
    @Lob
    @Column(nullable = false)
    private String content;

    /**
     * writer - 작성자
     * 공지사항을 작성한 관리자의 이름 또는 아이디입니다.
     * 예: "도서관 관리자", "admin"
     */
    @Column(nullable = false, length = 100)
    private String writer;

    /**
     * topFixed - 상단 고정 여부
     * true 이면 공지사항 목록 최상단에 고정 표시됩니다.
     * 중요 공지 또는 필독 공지에 사용됩니다.
     * 기본값: false (상단 고정 없음)
     */
    @Column(nullable = false)
    @Builder.Default
    private boolean topFixed = false;

    /**
     * regDate - 공지사항 등록일시
     * 공지사항이 처음 등록된 날짜와 시간입니다.
     */
    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime regDate = LocalDateTime.now();

    /**
     * images - 첨부 이미지 목록 (일대다 연관관계)
     * 이 공지사항에 첨부된 NoticeImage 객체 목록입니다.
     *
     * mappedBy = "notice" : NoticeImage 엔티티의 notice 필드가 연관관계 주인
     * cascade = ALL       : Notice 의 모든 생명주기 변화가 images 에 전파됨
     *                       (persist, merge, remove, refresh, detach)
     * orphanRemoval = true: images 목록에서 제거된 NoticeImage 는 DB에서도 자동 삭제
     * fetch = LAZY        : 공지사항 로딩 시 이미지는 즉시 로딩하지 않음
     *
     * @Builder.Default : Lombok @Builder 사용 시 기본값(new ArrayList<>())이
     *                    적용되도록 반드시 설정해야 합니다.
     */
    @OneToMany(mappedBy = "notice",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    @Builder.Default
    private List<NoticeImage> images = new ArrayList<>();

    // ──────────────────────────────────────────────
    // 비즈니스 메서드 (도메인 로직)
    // ──────────────────────────────────────────────

    /**
     * changeTitle - 제목 수정 메서드
     *
     * @param title 새 공지사항 제목
     */
    public void changeTitle(String title) {
        this.title = title;
    }

    /**
     * changeContent - 내용 수정 메서드
     *
     * @param content 새 공지사항 내용
     */
    public void changeContent(String content) {
        this.content = content;
    }

    /**
     * changeTopFixed - 상단 고정 여부 변경 메서드
     *
     * @param topFixed true: 상단 고정, false: 해제
     */
    public void changeTopFixed(boolean topFixed) {
        this.topFixed = topFixed;
    }

    /**
     * addImage - 이미지 추가 메서드
     * 이미지를 추가하면서 양방향 연관관계도 함께 설정합니다.
     * NoticeImage 의 notice 필드에 this(현재 Notice)를 설정합니다.
     *
     * @param image 추가할 NoticeImage 객체
     */
    public void addImage(NoticeImage image) {
        image.setNotice(this); // 양방향 관계 설정
        this.images.add(image);
    }

    /**
     * clearImages - 이미지 전체 삭제 메서드
     * 공지사항 수정 시 기존 이미지를 모두 제거하고 새 이미지를 추가할 때 사용합니다.
     * orphanRemoval = true 설정으로 목록에서 제거된 이미지는 DB에서도 삭제됩니다.
     */
    public void clearImages() {
        this.images.clear();
    }
}

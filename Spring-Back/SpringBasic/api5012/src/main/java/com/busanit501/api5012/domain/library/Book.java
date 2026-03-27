package com.busanit501.api5012.domain.library;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Book - 도서 엔티티
 *
 * 도서관에 소장된 도서 정보를 저장하는 JPA 엔티티입니다.
 * 도서 검색, 대여 가능 여부 확인, 도서 목록 조회 등에 사용됩니다.
 *
 * 연관관계:
 *   - Rental (1:N) : 해당 도서의 대여 이력 목록
 *
 * DB 테이블명: tbl_lib_book
 */
@Entity
@Table(name = "tbl_lib_book")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Book {

    /**
     * id - 기본키 (Primary Key)
     * DB에서 자동으로 증가하는 숫자형 식별자입니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * bookTitle - 도서명 (책 제목)
     * 검색 기능에서 LIKE 쿼리로 검색되는 핵심 필드입니다.
     * 예: "스프링 부트 핵심 가이드", "Clean Code"
     */
    @Column(nullable = false, length = 300)
    private String bookTitle;

    /**
     * author - 저자명
     * 저자명으로도 도서 검색이 가능합니다.
     * 예: "장정우", "Robert C. Martin"
     */
    @Column(nullable = false, length = 100)
    private String author;

    /**
     * publisher - 출판사명
     * 예: "한빛미디어", "인사이트"
     */
    @Column(length = 100)
    private String publisher;

    /**
     * isbn - 국제 표준 도서 번호 (International Standard Book Number)
     * 도서를 고유하게 식별하는 13자리 번호입니다. (ISBN-13 기준)
     * 중복 불가(unique) 제약 조건이 적용됩니다.
     * 예: "9791162243909"
     */
    @Column(unique = true, length = 20)
    private String isbn;

    /**
     * status - 도서 상태
     * BookStatus 열거형으로 현재 도서의 대여 가능 여부를 나타냅니다.
     * - AVAILABLE : 대여가능 (기본값)
     * - RENTED    : 대여중
     * - RESERVED  : 예약중
     * - LOST      : 분실
     * @EnumType.STRING 으로 저장하여 DB 가독성을 높입니다.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    @Builder.Default
    private BookStatus status = BookStatus.AVAILABLE;

    /**
     * description - 도서 설명 (책 소개)
     * 내용이 길 수 있으므로 @Lob (Large Object)로 선언합니다.
     * @Lob 은 CLOB(문자형 대용량 객체)로 저장되어 길이 제한이 없습니다.
     */
    @Lob
    private String description;

    /**
     * coverImage - 도서 표지 이미지 URL 또는 파일명
     * 외부 URL 또는 서버에 업로드된 파일명을 저장합니다.
     * null 이면 기본 표지 이미지를 사용합니다.
     */
    @Column(length = 500)
    private String coverImage;

    /**
     * publishDate - 출판일
     * LocalDate 타입으로 날짜만 저장합니다 (시간 불필요).
     * 예: 2023-05-15
     */
    private LocalDate publishDate;

    /**
     * regDate - 도서 등록일시 (도서관에 등록된 날짜)
     * 도서가 시스템에 처음 등록된 날짜와 시간을 기록합니다.
     */
    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime regDate = LocalDateTime.now();

    // ──────────────────────────────────────────────
    // 비즈니스 메서드 (도메인 로직)
    // ──────────────────────────────────────────────

    /**
     * changeStatus - 도서 상태 변경 메서드
     * 대여, 반납, 예약, 분실 처리 시 서비스 레이어에서 호출합니다.
     *
     * @param status 변경할 도서 상태 (BookStatus 열거형)
     */
    public void changeStatus(BookStatus status) {
        this.status = status;
    }

    /**
     * updateInfo - 도서 정보 수정 메서드
     * 관리자가 도서 정보를 수정할 때 사용합니다.
     *
     * @param bookTitle   수정할 도서명
     * @param author      수정할 저자명
     * @param publisher   수정할 출판사명
     * @param description 수정할 도서 설명
     */
    public void updateInfo(String bookTitle, String author, String publisher, String description) {
        this.bookTitle = bookTitle;
        this.author = author;
        this.publisher = publisher;
        this.description = description;
    }

    /**
     * isAvailable - 대여 가능 여부 확인 메서드
     * 도서 상태가 AVAILABLE 인 경우에만 true 를 반환합니다.
     *
     * @return 대여 가능하면 true, 불가능하면 false
     */
    public boolean isAvailable() {
        return this.status == BookStatus.AVAILABLE;
    }
}

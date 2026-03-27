package com.busanit501.api5012.dto.library;

import com.busanit501.api5012.domain.library.Book;
import com.busanit501.api5012.domain.library.BookStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * BookDTO - 도서 정보 DTO
 *
 * 도서 목록 조회, 상세 조회, 도서 등록/수정 요청에 사용되는 데이터 전송 객체입니다.
 * 등록/수정 시에는 id, status, regDate 를 제외한 나머지 필드를 요청 바디로 받습니다.
 *
 * API 엔드포인트:
 *   GET    /api/library/books      (도서 목록)
 *   GET    /api/library/books/{id} (도서 상세)
 *   POST   /api/library/books      (도서 등록, 관리자 전용)
 *   PUT    /api/library/books/{id} (도서 수정, 관리자 전용)
 *   DELETE /api/library/books/{id} (도서 삭제, 관리자 전용)
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    /** id - 도서 기본키 (응답 시에만 포함) */
    private Long id;

    /** bookTitle - 도서명 */
    private String bookTitle;

    /** author - 저자명 */
    private String author;

    /** publisher - 출판사명 */
    private String publisher;

    /** isbn - ISBN 번호 (13자리) */
    private String isbn;

    /**
     * status - 도서 상태
     * 응답 시 AVAILABLE, RENTED, RESERVED, LOST 중 하나
     */
    private BookStatus status;

    /** description - 도서 설명 */
    private String description;

    /** coverImage - 도서 표지 이미지 URL 또는 파일명 */
    private String coverImage;

    /**
     * publishDate - 출판일
     * @JsonFormat : "yyyy-MM-dd" 형식으로 직렬화
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate publishDate;

    /**
     * regDate - 도서 등록일시
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd HH:mm:ss",
                timezone = "Asia/Seoul")
    private LocalDateTime regDate;

    // ──────────────────────────────────────────────
    // 정적 팩토리 메서드
    // ──────────────────────────────────────────────

    /**
     * fromEntity - Book 엔티티를 BookDTO 로 변환
     *
     * @param book 변환할 Book 엔티티
     * @return 변환된 BookDTO
     */
    public static BookDTO fromEntity(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .bookTitle(book.getBookTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .isbn(book.getIsbn())
                .status(book.getStatus())
                .description(book.getDescription())
                .coverImage(book.getCoverImage())
                .publishDate(book.getPublishDate())
                .regDate(book.getRegDate())
                .build();
    }

    /**
     * toEntity - BookDTO 를 Book 엔티티로 변환 (등록 시 사용)
     * 상태(status) 는 기본값(AVAILABLE) 으로 자동 설정됩니다.
     *
     * @return 변환된 Book 엔티티
     */
    public Book toEntity() {
        return Book.builder()
                .bookTitle(this.bookTitle)
                .author(this.author)
                .publisher(this.publisher)
                .isbn(this.isbn)
                .description(this.description)
                .coverImage(this.coverImage)
                .publishDate(this.publishDate)
                .build();
    }
}

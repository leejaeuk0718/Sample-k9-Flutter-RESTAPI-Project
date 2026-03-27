package com.busanit501.api5012.dto.library;

import com.busanit501.api5012.domain.library.WishBook;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

/**
 * WishBookDTO - 희망 도서 신청 DTO
 *
 * 희망 도서 신청 내역 조회 및 신청 요청에 사용하는 데이터 전송 객체입니다.
 *
 * API 엔드포인트:
 *   GET  /api/library/wish-books/my     (내 희망 도서 신청 목록, 회원용)
 *   GET  /api/library/wish-books        (전체 신청 목록, 관리자용)
 *   POST /api/library/wish-books        (희망 도서 신청)
 *   PUT  /api/library/wish-books/{id}/approve (승인, 관리자)
 *   PUT  /api/library/wish-books/{id}/reject  (반려, 관리자)
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishBookDTO {

    /** id - 신청 기본키 */
    private Long id;

    /** memberId - 신청 회원 ID */
    private Long memberId;

    /** memberName - 신청 회원 이름 (평탄화) */
    private String memberName;

    /** wishBookTitle - 희망 도서 제목 */
    private String wishBookTitle;

    /** wishAuthor - 희망 도서 저자 */
    private String wishAuthor;

    /** wishPublisher - 희망 도서 출판사 */
    private String wishPublisher;

    /** reason - 신청 사유 */
    private String reason;

    /**
     * status - 처리 상태
     * "REQUESTED"(신청중), "APPROVED"(구입결정), "REJECTED"(반려)
     */
    private String status;

    /**
     * regDate - 신청 등록일시
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd HH:mm:ss",
                timezone = "Asia/Seoul")
    private LocalDateTime regDate;

    // ──────────────────────────────────────────────
    // 정적 팩토리 메서드
    // ──────────────────────────────────────────────

    /**
     * fromEntity - WishBook 엔티티를 WishBookDTO 로 변환
     *
     * @param wishBook 변환할 WishBook 엔티티
     * @return 변환된 WishBookDTO
     */
    public static WishBookDTO fromEntity(WishBook wishBook) {
        return WishBookDTO.builder()
                .id(wishBook.getId())
                .memberId(wishBook.getMember().getId())
                .memberName(wishBook.getMember().getMname())
                .wishBookTitle(wishBook.getWishBookTitle())
                .wishAuthor(wishBook.getWishAuthor())
                .wishPublisher(wishBook.getWishPublisher())
                .reason(wishBook.getReason())
                .status(wishBook.getStatus())
                .regDate(wishBook.getRegDate())
                .build();
    }
}

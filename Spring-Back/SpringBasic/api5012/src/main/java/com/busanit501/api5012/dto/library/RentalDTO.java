package com.busanit501.api5012.dto.library;

import com.busanit501.api5012.domain.library.Rental;
import com.busanit501.api5012.domain.library.RentalStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

/**
 * RentalDTO - 도서 대여 기록 DTO
 *
 * 대여 기록 조회 응답 시 사용하는 데이터 전송 객체입니다.
 * 연관 엔티티(Member, Book)의 주요 정보(이름, 제목)를 평탄화(flatten)하여 포함합니다.
 * 이렇게 하면 클라이언트가 추가 요청 없이 필요한 정보를 한 번에 받을 수 있습니다.
 *
 * [평탄화(Flatten) 설명]
 * Entity 구조:
 *   Rental → member.mname, book.bookTitle
 *
 * DTO 구조 (평탄화):
 *   RentalDTO → memberName, bookTitle (직접 포함)
 *
 * API 엔드포인트:
 *   GET /api/library/rentals/my      (내 대여 목록, 회원용)
 *   GET /api/library/rentals         (전체 대여 목록, 관리자용)
 *   POST /api/library/rentals        (대여 신청)
 *   PUT /api/library/rentals/{id}/return  (반납 처리)
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentalDTO {

    /** id - 대여 기록 기본키 */
    private Long id;

    /** memberId - 대여 회원 기본키 */
    private Long memberId;

    /**
     * memberName - 대여 회원 이름
     * member.mname 을 직접 포함하여 클라이언트의 추가 요청을 줄입니다.
     */
    private String memberName;

    /** memberMid - 대여 회원 아이디 */
    private String memberMid;

    /** bookId - 대여 도서 기본키 */
    private Long bookId;

    /**
     * bookTitle - 도서명
     * book.bookTitle 을 직접 포함합니다.
     */
    private String bookTitle;

    /** bookAuthor - 저자명 */
    private String bookAuthor;

    /**
     * rentalDate - 대여일
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate rentalDate;

    /**
     * dueDate - 반납 기한일
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    /**
     * returnDate - 실제 반납일 (대여 중이면 null)
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate returnDate;

    /**
     * status - 대여 상태
     * RENTING(대여중), RETURNED(반납완료), OVERDUE(연체), EXTENDED(연장)
     */
    private RentalStatus status;

    /**
     * overdue - 연체 여부 (편의 필드)
     * 클라이언트에서 연체 여부를 별도 계산 없이 바로 사용할 수 있도록 제공합니다.
     */
    private boolean overdue;

    // ──────────────────────────────────────────────
    // 정적 팩토리 메서드
    // ──────────────────────────────────────────────

    /**
     * fromEntity - Rental 엔티티를 RentalDTO 로 변환
     * member 와 book 이 LAZY 로딩이므로, 이 메서드 호출 전에
     * 트랜잭션 내에서 로딩되어 있어야 합니다.
     * (서비스 레이어의 @Transactional 내에서 호출)
     *
     * @param rental 변환할 Rental 엔티티
     * @return 변환된 RentalDTO
     */
    public static RentalDTO fromEntity(Rental rental) {
        return RentalDTO.builder()
                .id(rental.getId())
                .memberId(rental.getMember().getId())
                .memberName(rental.getMember().getMname())
                .memberMid(rental.getMember().getMid())
                .bookId(rental.getBook().getId())
                .bookTitle(rental.getBook().getBookTitle())
                .bookAuthor(rental.getBook().getAuthor())
                .rentalDate(rental.getRentalDate())
                .dueDate(rental.getDueDate())
                .returnDate(rental.getReturnDate())
                .status(rental.getStatus())
                .overdue(rental.isOverdue())
                .build();
    }
}

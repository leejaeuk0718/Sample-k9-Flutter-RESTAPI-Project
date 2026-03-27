package com.busanit501.api5012.repository.library;

import com.busanit501.api5012.domain.library.Book;
import com.busanit501.api5012.domain.library.BookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * BookRepository - 도서 레포지토리
 *
 * Book 엔티티에 대한 DB 접근 인터페이스입니다.
 * 도서 검색, 상태별 조회 등 도서관의 핵심 조회 기능을 제공합니다.
 *
 * Page<Book> 반환 타입:
 *   - 페이징 처리된 결과를 반환합니다.
 *   - Pageable 파라미터로 페이지 번호, 크기, 정렬 방식을 전달합니다.
 *   - 총 페이지 수, 총 데이터 수 등의 정보도 함께 제공됩니다.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * findByBookTitleContaining - 도서명으로 검색 (부분 일치, 페이징)
     * 도서 검색 기능의 핵심 메서드입니다.
     * Pageable 파라미터로 페이징 처리를 합니다.
     *
     * 생성되는 JPQL: SELECT b FROM Book b WHERE b.bookTitle LIKE %:bookTitle%
     *
     * @param bookTitle 검색할 도서명 키워드
     * @param pageable  페이징 정보 (페이지 번호, 크기, 정렬)
     * @return 페이징 처리된 도서 목록
     */
    Page<Book> findByBookTitleContaining(String bookTitle, Pageable pageable);

    /**
     * findByAuthorContaining - 저자명으로 검색 (부분 일치, 페이징)
     *
     * @param author   검색할 저자명 키워드
     * @param pageable 페이징 정보
     * @return 페이징 처리된 도서 목록
     */
    Page<Book> findByAuthorContaining(String author, Pageable pageable);

    /**
     * findByIsbn - ISBN으로 도서 조회
     * ISBN 은 도서를 고유하게 식별하는 번호이므로 Optional 로 반환합니다.
     *
     * @param isbn 조회할 ISBN 번호 (13자리)
     * @return 해당 ISBN 의 도서 (없으면 Optional.empty())
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * findByStatus - 도서 상태별 조회
     * 대여 가능한 도서, 대여 중인 도서 등을 조회합니다.
     *
     * @param status   조회할 도서 상태 (AVAILABLE, RENTED, RESERVED, LOST)
     * @param pageable 페이징 정보
     * @return 페이징 처리된 도서 목록
     */
    Page<Book> findByStatus(BookStatus status, Pageable pageable);

    /**
     * findByPublisherContaining - 출판사명으로 검색
     *
     * @param publisher 검색할 출판사명 키워드
     * @param pageable  페이징 정보
     * @return 페이징 처리된 도서 목록
     */
    Page<Book> findByPublisherContaining(String publisher, Pageable pageable);

    /**
     * countByStatus - 상태별 도서 수 집계
     * 관리자 대시보드에서 대여 가능 도서 수, 대여 중 도서 수 등을 표시합니다.
     *
     * @param status 집계할 도서 상태
     * @return 해당 상태의 도서 수
     */
    long countByStatus(BookStatus status);

    /**
     * searchByKeyword - 키워드로 도서 통합 검색 (도서명 + 저자명 + 출판사명)
     * 검색 키워드가 도서명, 저자명, 출판사명 중 하나라도 포함되면 결과에 포함됩니다.
     * @Query 로 직접 JPQL 을 작성하여 OR 조건 검색을 구현합니다.
     *
     * @param keyword  검색 키워드
     * @param pageable 페이징 정보
     * @return 키워드와 일치하는 페이징 처리된 도서 목록
     */
    @Query("SELECT b FROM Book b WHERE " +
           "b.bookTitle LIKE %:keyword% OR " +
           "b.author LIKE %:keyword% OR " +
           "b.publisher LIKE %:keyword%")
    Page<Book> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * searchByKeywordAndStatus - 키워드 + 상태 조합 검색
     * 대여 가능한 도서 중 특정 키워드를 검색할 때 사용합니다.
     *
     * @param keyword  검색 키워드
     * @param status   필터링할 도서 상태
     * @param pageable 페이징 정보
     * @return 조건에 맞는 페이징 처리된 도서 목록
     */
    @Query("SELECT b FROM Book b WHERE " +
           "(b.bookTitle LIKE %:keyword% OR " +
           "b.author LIKE %:keyword% OR " +
           "b.publisher LIKE %:keyword%) " +
           "AND b.status = :status")
    Page<Book> searchByKeywordAndStatus(@Param("keyword") String keyword,
                                        @Param("status") BookStatus status,
                                        Pageable pageable);

    /**
     * findAvailableBooks - 대여 가능 도서 목록 조회
     * 메인 페이지나 대여 신청 화면에서 대여 가능한 도서 목록을 표시합니다.
     *
     * @param pageable 페이징 정보
     * @return 대여 가능한 도서 목록
     */
    @Query("SELECT b FROM Book b WHERE b.status = 'AVAILABLE' ORDER BY b.regDate DESC")
    Page<Book> findAvailableBooks(Pageable pageable);

    /**
     * existsByIsbn - ISBN 중복 확인
     * 도서 등록 시 동일한 ISBN 의 도서가 이미 등록되어 있는지 확인합니다.
     *
     * @param isbn 확인할 ISBN
     * @return 이미 존재하면 true
     */
    boolean existsByIsbn(String isbn);

    /**
     * findByBookTitleContainingOrAuthorContaining - 도서명 또는 저자명 검색
     * 사용자가 입력한 키워드를 도서명과 저자명에서 동시에 검색합니다.
     *
     * @param title    도서명 검색 키워드
     * @param author   저자명 검색 키워드
     * @param pageable 페이징 정보
     * @return 조건에 맞는 도서 목록
     */
    Page<Book> findByBookTitleContainingOrAuthorContaining(
            String title, String author, Pageable pageable);

    /**
     * findAllByOrderByRegDateDesc - 최신 등록 도서 조회
     * 최근 등록된 도서를 최신순으로 조회합니다.
     * 메인 페이지의 "새로 들어온 도서" 섹션에 사용합니다.
     *
     * @param pageable 페이징 정보 (size=10 이면 최신 10권만 조회)
     * @return 최신순 정렬된 도서 목록
     */
    Page<Book> findAllByOrderByRegDateDesc(Pageable pageable);

    /**
     * findTop5ByStatusOrderByRegDateDesc - 상태별 최신 도서 5권 조회
     * 관리자 대시보드 등에서 최근 등록된 대여 가능 도서를 빠르게 표시합니다.
     *
     * @param status 조회할 도서 상태
     * @return 최신순 상위 5권
     */
    List<Book> findTop5ByStatusOrderByRegDateDesc(BookStatus status);
}

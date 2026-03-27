package com.busanit501.api5012.repository.library;

import com.busanit501.api5012.domain.library.LibraryEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * LibraryEventRepository - 도서관 행사 레포지토리
 *
 * LibraryEvent 엔티티에 대한 DB 접근 인터페이스입니다.
 * 행사 목록 조회, 기간별 검색, 카테고리별 조회 등의 기능을 제공합니다.
 */
@Repository
public interface LibraryEventRepository extends JpaRepository<LibraryEvent, Long> {

    /**
     * findByEventDateBetween - 기간 내 행사 조회
     * 특정 기간(startDate ~ endDate) 에 열리는 행사를 조회합니다.
     * 월별 행사 캘린더, 기간 행사 조회 등에 활용합니다.
     *
     * 생성되는 JPQL:
     *   SELECT e FROM LibraryEvent e
     *   WHERE e.eventDate BETWEEN :startDate AND :endDate
     *
     * @param startDate 조회 시작일
     * @param endDate   조회 종료일
     * @return 해당 기간의 행사 목록
     */
    List<LibraryEvent> findByEventDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * findByStatus - 신청 상태별 행사 조회 (페이징)
     * "OPEN" (신청 가능) 또는 "CLOSED" (마감) 상태 행사를 조회합니다.
     *
     * @param status   행사 상태 ("OPEN" 또는 "CLOSED")
     * @param pageable 페이징 정보
     * @return 해당 상태의 행사 목록 (페이징)
     */
    Page<LibraryEvent> findByStatus(String status, Pageable pageable);

    /**
     * findByCategory - 카테고리별 행사 조회 (페이징)
     * "문화행사", "주말극장", "강좌" 등 카테고리로 행사를 필터링합니다.
     *
     * @param category 조회할 카테고리명
     * @param pageable 페이징 정보
     * @return 해당 카테고리의 행사 목록 (페이징)
     */
    Page<LibraryEvent> findByCategory(String category, Pageable pageable);

    /**
     * findByCategoryAndStatus - 카테고리 + 상태 조합 조회
     * 특정 카테고리에서 신청 가능한 행사만 조회합니다.
     *
     * @param category 카테고리명
     * @param status   행사 상태
     * @param pageable 페이징 정보
     * @return 조건에 맞는 행사 목록 (페이징)
     */
    Page<LibraryEvent> findByCategoryAndStatus(String category, String status, Pageable pageable);

    /**
     * findUpcomingEvents - 오늘 이후 예정된 행사 조회 (최신순)
     * 메인 페이지 또는 행사 안내 화면에서 앞으로 열릴 행사 목록을 표시합니다.
     *
     * @param today    오늘 날짜 (LocalDate.now())
     * @param pageable 페이징 정보
     * @return 오늘 이후의 행사 목록 (행사일 오름차순)
     */
    @Query("SELECT e FROM LibraryEvent e " +
           "WHERE e.eventDate >= :today " +
           "ORDER BY e.eventDate ASC")
    Page<LibraryEvent> findUpcomingEvents(@Param("today") LocalDate today, Pageable pageable);

    /**
     * searchByKeyword - 키워드로 행사 검색 (제목 + 내용)
     * 행사 검색 기능에서 제목 또는 내용에 키워드가 포함된 행사를 조회합니다.
     *
     * @param keyword  검색 키워드
     * @param pageable 페이징 정보
     * @return 키워드와 일치하는 행사 목록 (페이징)
     */
    @Query("SELECT e FROM LibraryEvent e WHERE " +
           "e.title LIKE %:keyword% OR " +
           "e.content LIKE %:keyword% " +
           "ORDER BY e.eventDate DESC")
    Page<LibraryEvent> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * findByEventDateBetweenAndCategory - 기간 + 카테고리 조합 조회
     * 특정 기간 내에 특정 카테고리의 행사를 조회합니다.
     *
     * @param startDate 조회 시작일
     * @param endDate   조회 종료일
     * @param category  카테고리명
     * @return 조건에 맞는 행사 목록
     */
    List<LibraryEvent> findByEventDateBetweenAndCategory(
            LocalDate startDate, LocalDate endDate, String category);

    /**
     * countByStatus - 상태별 행사 수 집계
     * 관리자 대시보드에서 신청 가능한 행사 수를 표시합니다.
     *
     * @param status 행사 상태
     * @return 해당 상태의 행사 수
     */
    long countByStatus(String status);

    /**
     * findAllByOrderByEventDateAsc - 행사일 오름차순 전체 조회
     * 행사 캘린더 화면에서 가장 빠른 행사부터 표시합니다.
     *
     * @param pageable 페이징 정보
     * @return 행사일 오름차순 행사 목록 (페이징)
     */
    Page<LibraryEvent> findAllByOrderByEventDateAsc(Pageable pageable);
}

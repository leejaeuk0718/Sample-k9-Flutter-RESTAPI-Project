package com.busanit501.api5012.repository.library;

import com.busanit501.api5012.domain.library.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * NoticeRepository - 공지사항 레포지토리
 *
 * Notice 엔티티에 대한 DB 접근 인터페이스입니다.
 * 공지사항 목록 조회(상단 고정 포함), 검색, 상세 조회 기능을 제공합니다.
 */
@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    /**
     * findByTopFixedTrueOrderByRegDateDesc - 상단 고정 공지사항 조회 (최신순)
     * 공지사항 목록 상단에 고정 표시할 공지를 조회합니다.
     * topFixed = true 인 공지를 등록일시 내림차순으로 반환합니다.
     *
     * 메서드 이름 규칙 해석:
     *   - findBy       : SELECT 쿼리
     *   - TopFixed     : WHERE topFixed = ?
     *   - True         : = true (boolean 값)
     *   - OrderBy      : ORDER BY 절
     *   - RegDate      : regDate 컬럼
     *   - Desc         : 내림차순 (최신 먼저)
     *
     * @return 상단 고정 공지사항 목록 (최신순)
     */
    List<Notice> findByTopFixedTrueOrderByRegDateDesc();

    /**
     * findByTopFixedFalseOrderByRegDateDesc - 일반 공지사항 조회 (최신순, 페이징)
     * 상단 고정이 아닌 일반 공지사항을 최신순으로 조회합니다.
     *
     * @param pageable 페이징 정보
     * @return 일반 공지사항 목록 (페이징)
     */
    Page<Notice> findByTopFixedFalseOrderByRegDateDesc(Pageable pageable);

    /**
     * searchByKeyword - 공지사항 통합 검색 (제목 + 내용 + 작성자)
     * 검색 키워드가 제목, 내용, 작성자 중 하나라도 포함되면 결과에 포함됩니다.
     * 최신 등록순으로 정렬하여 반환합니다.
     *
     * @param keyword  검색 키워드
     * @param pageable 페이징 정보
     * @return 키워드와 일치하는 공지사항 목록 (페이징)
     */
    @Query("SELECT n FROM Notice n WHERE " +
           "n.title LIKE %:keyword% OR " +
           "n.content LIKE %:keyword% OR " +
           "n.writer LIKE %:keyword% " +
           "ORDER BY n.regDate DESC")
    Page<Notice> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * findByTitleContaining - 제목으로 공지사항 검색
     *
     * @param title    검색할 제목 키워드
     * @param pageable 페이징 정보
     * @return 제목에 키워드가 포함된 공지사항 목록 (페이징)
     */
    Page<Notice> findByTitleContaining(String title, Pageable pageable);

    /**
     * findWithImagesById - 이미지를 포함한 공지사항 상세 조회
     * 공지사항 상세 화면에서 첨부 이미지까지 함께 조회합니다.
     * JOIN FETCH 로 N+1 문제를 방지합니다.
     * (images 는 LAZY 로딩이므로 별도 fetch 없이는 추가 쿼리 발생)
     *
     * @param id 조회할 공지사항 ID
     * @return 이미지 포함 공지사항 (없으면 Optional.empty())
     */
    @Query("SELECT n FROM Notice n LEFT JOIN FETCH n.images WHERE n.id = :id")
    Optional<Notice> findWithImagesById(@Param("id") Long id);

    /**
     * findAllWithImages - 이미지 포함 공지사항 전체 조회 (페이징)
     * 목록 화면에서 썸네일 이미지도 함께 표시할 때 사용합니다.
     *
     * [주의] countQuery 를 별도로 지정해야 페이징이 정상 동작합니다.
     * JOIN FETCH + Page 조합은 Hibernate 가 count 쿼리를 자동 생성할 때
     * 문제가 생길 수 있으므로 countQuery 를 명시합니다.
     *
     * @param pageable 페이징 정보
     * @return 이미지 포함 공지사항 목록 (페이징)
     */
    @Query(value = "SELECT DISTINCT n FROM Notice n LEFT JOIN FETCH n.images ORDER BY n.regDate DESC",
           countQuery = "SELECT COUNT(n) FROM Notice n")
    Page<Notice> findAllWithImages(Pageable pageable);

    /**
     * findByWriter - 작성자별 공지사항 조회
     *
     * @param writer 조회할 작성자명
     * @return 해당 작성자의 공지사항 목록
     */
    List<Notice> findByWriter(String writer);
}

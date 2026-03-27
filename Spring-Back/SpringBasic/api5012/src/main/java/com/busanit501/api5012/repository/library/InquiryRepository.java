package com.busanit501.api5012.repository.library;

import com.busanit501.api5012.domain.library.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * InquiryRepository - 문의사항 레포지토리
 *
 * Inquiry 엔티티에 대한 DB 접근 인터페이스입니다.
 * 문의사항 목록 조회, 비밀글 처리, 답변 여부 필터링 등의 기능을 제공합니다.
 */
@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    /**
     * findByMemberId - 회원 ID 로 문의사항 조회 (페이징)
     * 마이페이지에서 내가 작성한 문의사항 목록을 표시합니다.
     * 최신 등록순으로 정렬합니다.
     *
     * @param memberId 조회할 회원 ID
     * @param pageable 페이징 정보
     * @return 해당 회원의 문의사항 목록 (페이징)
     */
    Page<Inquiry> findByMemberIdOrderByRegDateDesc(Long memberId, Pageable pageable);

    /**
     * findByWriter - 작성자명으로 문의사항 조회
     * 비회원 문의 또는 작성자명으로 검색할 때 사용합니다.
     *
     * @param writer   작성자명
     * @param pageable 페이징 정보
     * @return 해당 작성자의 문의사항 목록 (페이징)
     */
    Page<Inquiry> findByWriter(String writer, Pageable pageable);

    /**
     * findByAnswered - 답변 여부별 문의사항 조회 (관리자용)
     * 미답변 문의(answered = false)만 필터링하여 처리 우선순위를 파악합니다.
     *
     * @param answered 답변 여부 (true: 답변완료, false: 미답변)
     * @param pageable 페이징 정보
     * @return 해당 답변 상태의 문의사항 목록 (페이징)
     */
    Page<Inquiry> findByAnswered(boolean answered, Pageable pageable);

    /**
     * findPublicInquiries - 공개 문의사항 목록 조회 (비밀글 제외)
     * 일반 사용자에게 보여줄 공개 문의사항만 조회합니다.
     * secret = false 인 게시글만 반환합니다.
     *
     * @param pageable 페이징 정보
     * @return 공개 문의사항 목록 (페이징)
     */
    @Query("SELECT i FROM Inquiry i WHERE i.secret = false ORDER BY i.regDate DESC")
    Page<Inquiry> findPublicInquiries(Pageable pageable);

    /**
     * findInquiriesForMember - 특정 회원이 볼 수 있는 문의사항 목록
     * 공개 문의사항 + 해당 회원이 작성한 비밀 문의사항을 함께 조회합니다.
     * 로그인한 사용자의 문의사항 목록 화면에 사용합니다.
     *
     * @param memberId 로그인한 회원 ID
     * @param pageable 페이징 정보
     * @return 해당 회원이 볼 수 있는 문의사항 목록 (페이징)
     */
    @Query("SELECT i FROM Inquiry i WHERE " +
           "i.secret = false OR i.member.id = :memberId " +
           "ORDER BY i.regDate DESC")
    Page<Inquiry> findInquiriesForMember(@Param("memberId") Long memberId, Pageable pageable);

    /**
     * findWithRepliesById - 답변을 포함한 문의사항 상세 조회
     * 문의사항 상세 화면에서 답변 목록도 함께 표시합니다.
     * JOIN FETCH 로 N+1 문제를 방지합니다.
     *
     * @param id 조회할 문의사항 ID
     * @return 답변 목록이 포함된 문의사항 (없으면 Optional.empty())
     */
    @Query("SELECT i FROM Inquiry i LEFT JOIN FETCH i.replies WHERE i.id = :id")
    Optional<Inquiry> findWithRepliesById(@Param("id") Long id);

    /**
     * searchByKeyword - 키워드로 문의사항 검색 (제목 + 내용)
     * 공개 문의사항 중에서 키워드를 검색합니다.
     *
     * @param keyword  검색 키워드
     * @param pageable 페이징 정보
     * @return 검색 결과 목록 (페이징)
     */
    @Query("SELECT i FROM Inquiry i WHERE " +
           "(i.title LIKE %:keyword% OR i.content LIKE %:keyword%) " +
           "AND i.secret = false " +
           "ORDER BY i.regDate DESC")
    Page<Inquiry> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * countByAnswered - 답변 여부별 문의 건수 집계
     * 관리자 대시보드에서 미답변 문의 건수를 알림으로 표시합니다.
     *
     * @param answered 답변 여부
     * @return 해당 상태의 문의 건수
     */
    long countByAnswered(boolean answered);

    /**
     * findByMemberId - 회원의 문의사항 목록 (비밀글 포함, 마이페이지용)
     * 작성자 본인이 조회하므로 비밀글도 포함하여 반환합니다.
     *
     * @param memberId 회원 ID
     * @return 해당 회원의 전체 문의사항 목록
     */
    List<Inquiry> findByMemberId(Long memberId);
}

package com.busanit501.api5012.repository.library;

import com.busanit501.api5012.domain.library.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ReplyRepository - 문의사항 답변 레포지토리
 *
 * Reply 엔티티에 대한 DB 접근 인터페이스입니다.
 * 문의사항별 답변 조회, 답변 존재 여부 확인 등의 기능을 제공합니다.
 *
 * [참고]
 * 답변(Reply)은 문의사항(Inquiry)에 cascade 설정이 되어 있어
 * 문의사항 삭제 시 자동으로 함께 삭제됩니다.
 * 따라서 답변 단독 삭제는 이 레포지토리를 통해 직접 처리합니다.
 */
@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    /**
     * findByInquiryId - 문의사항 ID 로 답변 목록 조회
     * 문의사항 상세 화면에서 해당 문의에 달린 답변을 모두 조회합니다.
     * 등록일시 오름차순(ASC)으로 정렬하여 대화 흐름을 파악하기 쉽게 합니다.
     *
     * 생성되는 JPQL:
     *   SELECT r FROM Reply r
     *   WHERE r.inquiry.id = :inquiryId
     *   ORDER BY r.regDate ASC
     *
     * @param inquiryId 조회할 문의사항 ID
     * @return 해당 문의사항의 답변 목록 (등록일 오름차순)
     */
    List<Reply> findByInquiryIdOrderByRegDateAsc(Long inquiryId);

    /**
     * existsByInquiryId - 특정 문의사항에 답변이 있는지 확인
     * 문의사항 목록에서 답변 완료 여부를 빠르게 확인합니다.
     * count 쿼리를 사용하므로 findBy 보다 가볍습니다.
     *
     * @param inquiryId 확인할 문의사항 ID
     * @return 답변이 있으면 true, 없으면 false
     */
    boolean existsByInquiryId(Long inquiryId);

    /**
     * countByInquiryId - 특정 문의사항의 답변 수 집계
     * 문의사항에 달린 답변의 총 개수를 반환합니다.
     *
     * @param inquiryId 집계할 문의사항 ID
     * @return 답변 수
     */
    long countByInquiryId(Long inquiryId);

    /**
     * findByReplier - 답변 작성자별 조회
     * 특정 관리자가 작성한 모든 답변을 조회합니다.
     * 관리자별 답변 통계에 활용할 수 있습니다.
     *
     * @param replier 답변 작성자명
     * @return 해당 작성자의 답변 목록
     */
    List<Reply> findByReplier(String replier);

    /**
     * findByInquiryId - 문의사항 ID 로 답변 목록 조회 (정렬 없음)
     * 단순 답변 목록 조회 시 사용합니다.
     *
     * @param inquiryId 조회할 문의사항 ID
     * @return 해당 문의사항의 답변 목록
     */
    List<Reply> findByInquiryId(Long inquiryId);

    /**
     * findRepliesWithInquiry - 답변과 문의사항 정보를 함께 조회
     * 특정 문의사항의 답변을 문의 정보와 함께 로딩합니다.
     * JOIN FETCH 로 N+1 문제를 방지합니다.
     *
     * @param inquiryId 조회할 문의사항 ID
     * @return 문의사항 정보가 포함된 답변 목록
     */
    @Query("SELECT r FROM Reply r " +
           "JOIN FETCH r.inquiry i " +
           "WHERE i.id = :inquiryId " +
           "ORDER BY r.regDate ASC")
    List<Reply> findRepliesWithInquiry(@Param("inquiryId") Long inquiryId);
}

package com.busanit501.api5012.repository.library;

import com.busanit501.api5012.domain.library.Member;
import com.busanit501.api5012.domain.library.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MemberRepository - 도서관 회원 레포지토리
 *
 * Member 엔티티에 대한 DB 접근 인터페이스입니다.
 * JpaRepository 를 상속받아 기본 CRUD 메서드를 자동으로 제공받습니다.
 *
 * JpaRepository<Member, Long> 의 의미:
 *   - Member : 관리할 엔티티 타입
 *   - Long   : 기본키(PK) 타입
 *
 * Spring Data JPA 의 메서드 이름 규칙(Query Method)을 활용하여
 * 별도의 SQL/JPQL 작성 없이 쿼리 메서드를 자동 생성합니다.
 * 예: findByMid(String mid) → SELECT * FROM tbl_lib_member WHERE mid = ?
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * findByMid - 아이디로 회원 조회
     * 로그인 처리 시 입력한 아이디로 회원 정보를 조회합니다.
     * Optional 을 반환하여 회원이 없는 경우 NullPointerException 을 방지합니다.
     *
     * 생성되는 JPQL: SELECT m FROM Member m WHERE m.mid = :mid
     *
     * @param mid 조회할 회원 아이디
     * @return 해당 아이디의 회원 (없으면 Optional.empty())
     */
    Optional<Member> findByMid(String mid);

    /**
     * existsByMid - 아이디 중복 확인
     * 회원가입 시 입력한 아이디가 이미 사용 중인지 확인합니다.
     * count 쿼리를 사용하므로 findBy 보다 가볍습니다.
     *
     * 생성되는 JPQL: SELECT COUNT(m) > 0 FROM Member m WHERE m.mid = :mid
     *
     * @param mid 중복 확인할 아이디
     * @return 이미 존재하면 true, 사용 가능하면 false
     */
    boolean existsByMid(String mid);

    /**
     * existsByEmail - 이메일 중복 확인
     * 회원가입 또는 이메일 변경 시 입력한 이메일이 이미 사용 중인지 확인합니다.
     *
     * @param email 중복 확인할 이메일
     * @return 이미 존재하면 true, 사용 가능하면 false
     */
    boolean existsByEmail(String email);

    /**
     * findByEmail - 이메일로 회원 조회
     * 비밀번호 찾기 기능에서 이메일로 회원을 조회할 때 사용합니다.
     *
     * @param email 조회할 이메일
     * @return 해당 이메일의 회원 (없으면 Optional.empty())
     */
    Optional<Member> findByEmail(String email);

    /**
     * findByRole - 역할(권한)별 회원 목록 조회
     * 관리자 목록 또는 일반 회원 목록을 조회할 때 사용합니다.
     *
     * @param role 조회할 회원 역할 (MemberRole.USER 또는 MemberRole.ADMIN)
     * @return 해당 역할의 회원 목록
     */
    List<Member> findByRole(MemberRole role);

    /**
     * findByMnameContaining - 이름으로 회원 검색 (부분 일치)
     * 관리자 화면에서 회원을 이름으로 검색할 때 사용합니다.
     * Containing 키워드는 SQL 의 LIKE '%keyword%' 와 동일합니다.
     *
     * 생성되는 JPQL: SELECT m FROM Member m WHERE m.mname LIKE %:mname%
     *
     * @param mname 검색할 이름 키워드
     * @return 이름에 키워드가 포함된 회원 목록
     */
    List<Member> findByMnameContaining(String mname);

    /**
     * findByRegion - 지역별 회원 조회
     * 특정 지역 회원 통계 및 조회에 사용합니다.
     *
     * @param region 조회할 지역명
     * @return 해당 지역의 회원 목록
     */
    List<Member> findByRegion(String region);

    /**
     * countByRole - 역할별 회원 수 집계
     * 관리자 대시보드에서 전체 회원 수, 관리자 수 등을 표시할 때 사용합니다.
     *
     * @param role 집계할 역할
     * @return 해당 역할의 회원 수
     */
    long countByRole(MemberRole role);

    /**
     * findByMidAndEmail - 아이디와 이메일로 회원 조회
     * 비밀번호 찾기 기능에서 아이디와 이메일이 모두 일치하는 회원을 조회합니다.
     *
     * @param mid   조회할 아이디
     * @param email 조회할 이메일
     * @return 아이디와 이메일이 모두 일치하는 회원
     */
    Optional<Member> findByMidAndEmail(String mid, String email);

    /**
     * searchByKeyword - 키워드로 회원 통합 검색
     * 아이디, 이름, 이메일 중 하나라도 키워드를 포함하는 회원을 검색합니다.
     * @Query 어노테이션으로 직접 JPQL 을 작성합니다.
     *
     * @param keyword 검색 키워드
     * @return 키워드와 일치하는 회원 목록
     */
    @Query("SELECT m FROM Member m WHERE " +
           "m.mid LIKE %:keyword% OR " +
           "m.mname LIKE %:keyword% OR " +
           "m.email LIKE %:keyword%")
    List<Member> searchByKeyword(@Param("keyword") String keyword);
}

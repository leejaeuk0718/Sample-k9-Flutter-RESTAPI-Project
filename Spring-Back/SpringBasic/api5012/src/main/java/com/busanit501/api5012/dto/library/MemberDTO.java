package com.busanit501.api5012.dto.library;

import com.busanit501.api5012.domain.library.Member;
import com.busanit501.api5012.domain.library.MemberRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

/**
 * MemberDTO - 회원 정보 응답 DTO
 *
 * REST API 응답 시 회원 정보를 전달하는 데이터 전송 객체입니다.
 * 보안상 비밀번호(mpw)는 절대 포함하지 않습니다.
 *
 * [DTO 와 Entity 의 차이]
 * - Entity : DB 테이블과 직접 매핑되는 JPA 객체 (도메인 계층)
 * - DTO    : 계층 간 데이터 전달용 객체 (표현 계층 ↔ 서비스 계층)
 *
 * Entity 를 직접 반환하면 다음 문제가 발생합니다:
 *   1. 비밀번호 등 민감 정보 노출
 *   2. 순환 참조 (JSON 직렬화 시 StackOverflow)
 *   3. LAZY 로딩으로 인한 LazyInitializationException
 *   4. API 스펙과 DB 구조가 강하게 결합됨
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    /** id - 회원 기본키 */
    private Long id;

    /** mid - 회원 아이디 */
    private String mid;

    /** mname - 회원 이름 */
    private String mname;

    /** email - 이메일 주소 */
    private String email;

    /** region - 거주 지역 */
    private String region;

    /** role - 회원 역할 (USER / ADMIN) */
    private MemberRole role;

    /** profileImg - 프로필 이미지 파일명 */
    private String profileImg;

    /**
     * regDate - 가입일시
     * @JsonFormat : JSON 직렬화 시 날짜 형식을 지정합니다.
     * pattern = "yyyy-MM-dd HH:mm:ss" : 2024-03-15 14:30:00 형태로 출력
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd HH:mm:ss",
                timezone = "Asia/Seoul")
    private LocalDateTime regDate;

    // ──────────────────────────────────────────────
    // 정적 팩토리 메서드 (Static Factory Method)
    // ──────────────────────────────────────────────

    /**
     * fromEntity - Member 엔티티를 MemberDTO 로 변환하는 정적 메서드
     *
     * 사용법:
     *   Member member = memberRepository.findById(1L).get();
     *   MemberDTO dto = MemberDTO.fromEntity(member);
     *
     * @param member 변환할 Member 엔티티
     * @return 변환된 MemberDTO (비밀번호 제외)
     */
    public static MemberDTO fromEntity(Member member) {
        return MemberDTO.builder()
                .id(member.getId())
                .mid(member.getMid())
                .mname(member.getMname())
                .email(member.getEmail())
                .region(member.getRegion())
                .role(member.getRole())
                .profileImg(member.getProfileImg())
                .regDate(member.getRegDate())
                .build();
    }
}

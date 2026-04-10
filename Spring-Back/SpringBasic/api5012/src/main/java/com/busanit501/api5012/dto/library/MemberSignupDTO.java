package com.busanit501.api5012.dto.library;

import lombok.*;

/**
 * MemberSignupDTO - 회원가입 요청 DTO
 *
 * 클라이언트(Flutter 앱)에서 회원가입 요청 시 전송하는 데이터를 담습니다.
 * 서비스 레이어에서 mpw 를 BCrypt 로 암호화한 후 Member 엔티티를 생성합니다.
 *
 * API 엔드포인트: POST /api/library/members/signup
 *
 * 요청 JSON 예시:
 * {
 *   "mid": "user01",
 *   "mpw": "password123!",
 *   "mpwConfirm": "password123!",
 *   "mname": "홍길동",
 *   "email": "hong@example.com",
 *   "region": "부산광역시 해운대구"
 * }
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignupDTO {

    /**
     * mid - 가입할 아이디
     * 중복 확인이 필요합니다. (MemberRepository.existsByMid() 로 검증)
     * 유효성 검사: 영문+숫자, 4~20자
     */
    private String mid;

    /**
     * mpw - 비밀번호 (평문)
     * 서비스 레이어에서 BCryptPasswordEncoder 로 암호화 후 저장합니다.
     * 유효성 검사: 최소 8자, 영문+숫자+특수문자 조합 권장
     */
    private String mpw;

    /**
     * mpwConfirm - 비밀번호 확인
     * mpw 와 동일해야 합니다.
     * 서비스 레이어에서 mpw.equals(mpwConfirm) 으로 검증합니다.
     * DB에는 저장하지 않습니다.
     */
    private String mpwConfirm;

    /**
     * mname - 회원 이름 (실명)
     */
    private String mname;

    /**
     * email - 이메일 주소
     * 중복 확인이 필요합니다. (MemberRepository.existsByEmail() 로 검증)
     */
    private String email;

    /**
     * region - 거주 지역
     * 선택 사항입니다. (nullable)
     * 예: "부산광역시 남구"
     */
    private String region;

    /**
     * profileImageBase64 - 프로필 이미지 (base64 인코딩)
     * 선택 사항입니다. null 이면 프로필 이미지 없이 가입합니다.
     * 서버에서 디코딩 후 c:/upload/springTest 폴더에 UUID 파일명으로 저장합니다.
     * 최대 허용 크기: 5MB (클라이언트에서 1차 검사, 서버에서 2차 검사)
     */
    private String profileImageBase64;
}

package com.busanit501.api5012.dto.library;

import lombok.*;

/**
 * MemberLoginDTO - 로그인 요청 DTO
 *
 * 클라이언트에서 로그인 요청 시 전송하는 아이디와 비밀번호를 담습니다.
 * 기존 JWT 인증 시스템(APILoginFilter)과 별개로, 도서관 회원 전용 로그인에 사용합니다.
 *
 * API 엔드포인트: POST /api/library/members/login
 *
 * 요청 JSON 예시:
 * {
 *   "mid": "user01",
 *   "mpw": "password123!"
 * }
 *
 * 응답: JWT 액세스 토큰 + 리프레시 토큰
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginDTO {

    /**
     * mid - 로그인 아이디
     */
    private String mid;

    /**
     * mpw - 로그인 비밀번호 (평문)
     * 서비스 레이어에서 BCryptPasswordEncoder.matches() 로 DB 암호화 값과 비교합니다.
     * 절대로 DB에 평문으로 저장되지 않습니다.
     */
    private String mpw;
}

package com.busanit501.api5012.controller.library;

import com.busanit501.api5012.dto.library.MemberDTO;
import com.busanit501.api5012.dto.library.MemberSignupDTO;
import com.busanit501.api5012.service.library.MemberLibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * LibraryMemberController - 도서관 회원 REST 컨트롤러
 *
 * 부산도서관 관리 시스템의 회원 관련 API 엔드포인트를 제공합니다.
 * 기본 경로: /api/member/
 *
 * [JWT 로그인 안내]
 * 실제 로그인(JWT 토큰 발급)은 /generateToken 엔드포인트에서 처리됩니다.
 * APILoginFilter → APILoginSuccessHandler → JWTUtil.generateToken() 흐름으로 처리됩니다.
 * 이 컨트롤러의 회원가입 후 /generateToken 으로 로그인하면 됩니다.
 *
 * [ResponseEntity 설명]
 * ResponseEntity<T> 는 HTTP 응답(상태코드 + 헤더 + 바디)을 직접 제어할 수 있는 래퍼 클래스입니다.
 * - ResponseEntity.ok(body)             : 200 OK + 바디
 * - ResponseEntity.status(201).body(x)  : 201 Created + 바디
 * - ResponseEntity.badRequest().body(x) : 400 Bad Request + 바디
 *
 * [Swagger(@Tag, @Operation) 설명]
 * @Tag      : Swagger UI에서 API 그룹 이름을 설정합니다.
 * @Operation: 각 엔드포인트의 설명을 Swagger UI에 표시합니다.
 */
@Slf4j
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Tag(name = "회원 관리 API", description = "부산도서관 회원가입, 정보조회, 수정 API")
public class LibraryMemberController {

    /** MemberLibraryService - 회원 비즈니스 로직 서비스 */
    private final MemberLibraryService memberLibraryService;

    // ──────────────────────────────────────────────────────
    // POST /api/member/signup  →  회원가입
    // ──────────────────────────────────────────────────────

    /**
     * signup - 회원가입 API
     *
     * 클라이언트에서 회원 정보를 JSON 으로 전송하면
     * BCrypt 암호화 후 DB에 저장하고 생성된 회원 ID 를 반환합니다.
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
     *
     * @param dto 회원가입 요청 정보
     * @return 201 Created + { "memberId": 1 }
     */
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "회원가입", description = "새 회원을 등록합니다. 아이디/이메일 중복 체크 및 BCrypt 암호화가 적용됩니다.")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody MemberSignupDTO dto) {
        log.info("회원가입 요청 - mid: {}", dto.getMid());

        try {
            Long memberId = memberLibraryService.signup(dto);
            // 201 Created 상태코드로 반환 (리소스 생성 성공)
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "result", "success",
                            "memberId", memberId,
                            "message", "회원가입이 완료되었습니다."
                    ));
        } catch (IllegalArgumentException e) {
            // 아이디/이메일 중복, 비밀번호 불일치 등 유효성 오류
            log.warn("회원가입 실패 - {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("result", "error", "message", e.getMessage()));
        }
    }

    // ──────────────────────────────────────────────────────
    // GET /api/member/me?mid={mid}  →  내 정보 조회
    // ──────────────────────────────────────────────────────

    /**
     * getMyInfo - 회원 정보 조회 API
     *
     * 아이디(mid)로 회원 정보를 조회합니다.
     * 비밀번호는 MemberDTO 에 포함되지 않으므로 안전합니다.
     *
     * 요청 예시: GET /api/member/me?mid=user01
     *
     * @param mid 조회할 회원 아이디 (쿼리 파라미터)
     * @return 200 OK + MemberDTO
     */
    @GetMapping("/me")
    @Operation(summary = "회원 정보 조회", description = "아이디(mid)로 회원 정보를 조회합니다.")
    public ResponseEntity<Object> getMyInfo(
            @Parameter(description = "조회할 회원 아이디")
            @RequestParam String mid) {
        log.info("회원 정보 조회 요청 - mid: {}", mid);

        try {
            MemberDTO memberDTO = memberLibraryService.getMemberByMid(mid);
            return ResponseEntity.ok(memberDTO);
        } catch (RuntimeException e) {
            log.warn("회원 정보 조회 실패 - mid: {}, 오류: {}", mid, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("result", "error", "message", e.getMessage()));
        }
    }

    // ──────────────────────────────────────────────────────
    // PUT /api/member/update  →  회원 정보 수정
    // ──────────────────────────────────────────────────────

    /**
     * updateMember - 회원 정보 수정 API
     *
     * 이메일, 지역 정보를 수정합니다.
     *
     * 요청 JSON 예시:
     * {
     *   "mid": "user01",
     *   "email": "newemail@example.com",
     *   "region": "부산광역시 남구"
     * }
     *
     * @param dto 수정할 회원 정보 (mid 필수)
     * @return 200 OK + { "result": "success" }
     */
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "회원 정보 수정", description = "이메일, 거주 지역을 수정합니다.")
    public ResponseEntity<Map<String, String>> updateMember(@RequestBody MemberDTO dto) {
        log.info("회원 정보 수정 요청 - mid: {}", dto.getMid());

        try {
            memberLibraryService.updateMember(dto.getMid(), dto);
            return ResponseEntity.ok(Map.of("result", "success", "message", "회원 정보가 수정되었습니다."));
        } catch (IllegalArgumentException e) {
            log.warn("회원 정보 수정 실패 - {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("result", "error", "message", e.getMessage()));
        } catch (RuntimeException e) {
            log.warn("회원 정보 수정 실패 - {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("result", "error", "message", e.getMessage()));
        }
    }

    // ──────────────────────────────────────────────────────
    // PUT /api/member/profile-image  →  프로필 이미지 변경
    // ──────────────────────────────────────────────────────

    /**
     * updateProfileImage - 프로필 이미지 변경 API
     *
     * 이미지 업로드 후 저장된 파일명을 전달하여 프로필 이미지를 업데이트합니다.
     *
     * 요청 JSON 예시:
     * {
     *   "mid": "user01",
     *   "profileImg": "550e8400-e29b-41d4-a716-446655440000.jpg"
     * }
     *
     * @param body { "mid": "...", "profileImg": "..." }
     * @return 200 OK + { "result": "success" }
     */
    @PutMapping(value = "/profile-image", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "프로필 이미지 변경", description = "프로필 이미지 파일명을 업데이트합니다.")
    public ResponseEntity<Map<String, String>> updateProfileImage(
            @RequestBody Map<String, String> body) {
        String mid = body.get("mid");
        String profileImg = body.get("profileImg");
        log.info("프로필 이미지 변경 요청 - mid: {}", mid);

        try {
            memberLibraryService.updateProfileImage(mid, profileImg);
            return ResponseEntity.ok(Map.of("result", "success", "message", "프로필 이미지가 변경되었습니다."));
        } catch (RuntimeException e) {
            log.warn("프로필 이미지 변경 실패 - {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("result", "error", "message", e.getMessage()));
        }
    }

    // ──────────────────────────────────────────────────────
    // GET /api/member/check-mid?mid={mid}  →  아이디 중복체크
    // ──────────────────────────────────────────────────────

    /**
     * checkMid - 아이디 중복 체크 API
     *
     * 회원가입 전 아이디 사용 가능 여부를 확인합니다.
     * { "available": true }  → 사용 가능
     * { "available": false } → 이미 사용 중
     *
     * @param mid 중복 확인할 아이디
     * @return 200 OK + { "available": true/false }
     */
    @GetMapping("/check-mid")
    @Operation(summary = "아이디 중복 체크", description = "입력한 아이디의 사용 가능 여부를 반환합니다.")
    public ResponseEntity<Map<String, Boolean>> checkMid(
            @Parameter(description = "중복 확인할 아이디")
            @RequestParam String mid) {
        log.info("아이디 중복 체크 요청 - mid: {}", mid);
        boolean isDuplicate = memberLibraryService.checkDuplicateMid(mid);
        // available = 중복 아님 (사용 가능)
        return ResponseEntity.ok(Map.of("available", !isDuplicate));
    }

    // ──────────────────────────────────────────────────────
    // GET /api/member/check-email?email={email}  →  이메일 중복체크
    // ──────────────────────────────────────────────────────

    /**
     * checkEmail - 이메일 중복 체크 API
     *
     * @param email 중복 확인할 이메일
     * @return 200 OK + { "available": true/false }
     */
    @GetMapping("/check-email")
    @Operation(summary = "이메일 중복 체크", description = "입력한 이메일의 사용 가능 여부를 반환합니다.")
    public ResponseEntity<Map<String, Boolean>> checkEmail(
            @Parameter(description = "중복 확인할 이메일")
            @RequestParam String email) {
        log.info("이메일 중복 체크 요청 - email: {}", email);
        boolean isDuplicate = memberLibraryService.checkDuplicateEmail(email);
        return ResponseEntity.ok(Map.of("available", !isDuplicate));
    }
}

package com.busanit501.api5012.controller.library;

import com.busanit501.api5012.dto.library.WishBookDTO;
import com.busanit501.api5012.service.library.WishBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * WishBookController - 희망도서 신청 REST 컨트롤러
 *
 * 부산도서관 관리 시스템의 희망도서 신청 관련 API 엔드포인트를 제공합니다.
 * 기본 경로: /api/wishbook/
 *
 * [엔드포인트 목록]
 * POST /api/wishbook              - 희망도서 신청
 * GET  /api/wishbook/my           - 내 희망도서 목록 조회
 * PUT  /api/wishbook/{id}/approve - 희망도서 승인 (관리자)
 * PUT  /api/wishbook/{id}/reject  - 희망도서 거절 (관리자)
 *
 * [처리 흐름]
 * 1. POST /api/wishbook              → 신청 (PENDING 상태)
 * 2. PUT  /api/wishbook/{id}/approve → 관리자 승인 (APPROVED 상태)
 * 3. PUT  /api/wishbook/{id}/reject  → 관리자 거절 (REJECTED 상태)
 *
 * [중복 신청 방지]
 * 동일 회원이 같은 제목의 도서를 중복으로 신청할 수 없습니다.
 */
@Slf4j
@RestController
@RequestMapping("/api/wishbook")
@RequiredArgsConstructor
@Tag(name = "희망도서 신청 API", description = "희망도서 신청, 내 목록 조회, 승인/거절 API")
public class WishBookController {

    /** WishBookService - 희망도서 비즈니스 로직 서비스 */
    private final WishBookService wishBookService;

    // ──────────────────────────────────────────────────────
    // POST /api/wishbook  →  희망도서 신청
    // ──────────────────────────────────────────────────────

    /**
     * createWishBook - 희망도서 신청 API
     *
     * 회원이 희망도서를 신청합니다.
     * 동일 회원이 같은 도서 제목으로 중복 신청할 수 없습니다.
     *
     * 요청 JSON 예시:
     * {
     *   "wishBookTitle": "스프링 부트 핵심 가이드",
     *   "wishAuthor": "김영한",
     *   "wishPublisher": "위키북스",
     *   "reason": "백엔드 개발 학습에 필요합니다."
     * }
     *
     * @param memberId 신청 회원 ID (쿼리 파라미터)
     * @param dto      희망도서 신청 정보
     * @return 201 Created + { "wishBookId": 1 }
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "희망도서 신청", description = "회원이 희망도서를 신청합니다. 같은 제목의 중복 신청은 불가합니다.")
    public ResponseEntity<Map<String, Object>> createWishBook(
            @Parameter(description = "신청 회원 ID")
            @RequestParam Long memberId,
            @RequestBody WishBookDTO dto) {
        log.info("희망도서 신청 요청 - memberId: {}, 도서명: {}", memberId, dto.getWishBookTitle());

        try {
            Long wishBookId = wishBookService.createWishBook(dto, memberId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "result", "success",
                            "wishBookId", wishBookId,
                            "message", "희망도서 신청이 완료되었습니다."
                    ));
        } catch (IllegalStateException e) {
            // 중복 신청 등
            log.warn("희망도서 신청 실패 - {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("result", "error", "message", e.getMessage()));
        } catch (RuntimeException e) {
            log.warn("희망도서 신청 오류 - {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("result", "error", "message", e.getMessage()));
        }
    }

    // ──────────────────────────────────────────────────────
    // GET /api/wishbook/my?memberId={}  →  내 희망도서 목록 조회
    // ──────────────────────────────────────────────────────

    /**
     * getMyWishBooks - 내 희망도서 목록 조회 API
     *
     * 특정 회원의 희망도서 신청 이력을 페이지 단위로 반환합니다.
     * 신청일 기준 내림차순으로 반환합니다.
     *
     * 요청 예시: GET /api/wishbook/my?memberId=1&page=0&size=10
     *
     * @param memberId 조회할 회원 ID
     * @param page     페이지 번호 (0부터 시작)
     * @param size     페이지 크기
     * @return 200 OK + Page<WishBookDTO>
     */
    @GetMapping("/my")
    @Operation(summary = "내 희망도서 목록 조회", description = "회원의 희망도서 신청 이력을 페이지 단위로 반환합니다.")
    public ResponseEntity<Page<WishBookDTO>> getMyWishBooks(
            @Parameter(description = "조회할 회원 ID")
            @RequestParam Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("내 희망도서 목록 조회 - memberId: {}, page: {}", memberId, page);

        Pageable pageable = PageRequest.of(page, size, Sort.by("regDate").descending());
        Page<WishBookDTO> wishBooks = wishBookService.getMyWishBooks(memberId, pageable);
        return ResponseEntity.ok(wishBooks);
    }

    // ──────────────────────────────────────────────────────
    // PUT /api/wishbook/{id}/approve  →  희망도서 승인 (관리자)
    // ──────────────────────────────────────────────────────

    /**
     * approveWishBook - 희망도서 승인 API (관리자 전용)
     *
     * PENDING 상태의 희망도서 신청을 APPROVED 로 변경합니다.
     * 이미 처리된 신청(APPROVED/REJECTED)은 승인할 수 없습니다.
     *
     * @param id 승인할 희망도서 신청 기본키
     * @return 200 OK + { "result": "success" }
     */
    @PutMapping("/{id}/approve")
    @Operation(summary = "희망도서 승인 (관리자)", description = "PENDING 상태의 희망도서 신청을 APPROVED로 변경합니다.")
    public ResponseEntity<Map<String, String>> approveWishBook(
            @Parameter(description = "승인할 희망도서 신청 기본키")
            @PathVariable Long id) {
        log.info("희망도서 승인 요청 - wishBookId: {}", id);

        try {
            wishBookService.approveWishBook(id);
            return ResponseEntity.ok(Map.of("result", "success", "message", "희망도서 신청이 승인되었습니다."));
        } catch (IllegalStateException e) {
            // 이미 처리된 신청
            log.warn("희망도서 승인 불가 - wishBookId: {}, 사유: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("result", "error", "message", e.getMessage()));
        } catch (RuntimeException e) {
            log.warn("희망도서 승인 실패 - wishBookId: {}, 오류: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("result", "error", "message", e.getMessage()));
        }
    }

    // ──────────────────────────────────────────────────────
    // PUT /api/wishbook/{id}/reject  →  희망도서 거절 (관리자)
    // ──────────────────────────────────────────────────────

    /**
     * rejectWishBook - 희망도서 거절 API (관리자 전용)
     *
     * PENDING 상태의 희망도서 신청을 REJECTED 로 변경합니다.
     * 이미 처리된 신청(APPROVED/REJECTED)은 거절할 수 없습니다.
     *
     * @param id 거절할 희망도서 신청 기본키
     * @return 200 OK + { "result": "success" }
     */
    @PutMapping("/{id}/reject")
    @Operation(summary = "희망도서 거절 (관리자)", description = "PENDING 상태의 희망도서 신청을 REJECTED로 변경합니다.")
    public ResponseEntity<Map<String, String>> rejectWishBook(
            @Parameter(description = "거절할 희망도서 신청 기본키")
            @PathVariable Long id) {
        log.info("희망도서 거절 요청 - wishBookId: {}", id);

        try {
            wishBookService.rejectWishBook(id);
            return ResponseEntity.ok(Map.of("result", "success", "message", "희망도서 신청이 거절되었습니다."));
        } catch (IllegalStateException e) {
            // 이미 처리된 신청
            log.warn("희망도서 거절 불가 - wishBookId: {}, 사유: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("result", "error", "message", e.getMessage()));
        } catch (RuntimeException e) {
            log.warn("희망도서 거절 실패 - wishBookId: {}, 오류: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("result", "error", "message", e.getMessage()));
        }
    }
}

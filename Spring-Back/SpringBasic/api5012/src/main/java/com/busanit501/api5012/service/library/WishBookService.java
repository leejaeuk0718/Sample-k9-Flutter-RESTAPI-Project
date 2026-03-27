package com.busanit501.api5012.service.library;

import com.busanit501.api5012.dto.library.WishBookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * WishBookService - 희망 도서 신청 서비스 인터페이스
 *
 * 부산도서관 관리 시스템의 희망 도서 신청 비즈니스 로직을 정의합니다.
 * 회원이 도서관에 구입을 희망하는 도서를 신청하고, 관리자가 처리하는 기능을 제공합니다.
 *
 * [처리 상태 흐름]
 * REQUESTED(신청중) → APPROVED(구입결정) : 도서관 측 구입 결정
 * REQUESTED(신청중) → REJECTED(반려)     : 절판, 예산 부족 등의 사유로 반려
 */
public interface WishBookService {

    /**
     * createWishBook - 희망 도서 신청 등록
     *
     * 1. 회원 존재 여부 확인
     * 2. 동일 도서 중복 신청 여부 확인
     * 3. WishBook 엔티티 생성 (상태: REQUESTED)
     *
     * @param dto      희망 도서 정보 (제목, 저자, 출판사, 신청 사유)
     * @param memberId 신청 회원 ID
     * @return 생성된 희망 도서 신청 ID
     * @throws IllegalStateException 동일 도서 중복 신청 시
     */
    Long createWishBook(WishBookDTO dto, Long memberId);

    /**
     * getMyWishBooks - 내 희망 도서 신청 목록 조회
     *
     * 마이페이지에서 내가 신청한 희망 도서 목록을 조회합니다.
     *
     * @param memberId 조회할 회원 ID
     * @param pageable 페이지 정보
     * @return 페이지네이션이 적용된 희망 도서 신청 목록
     */
    Page<WishBookDTO> getMyWishBooks(Long memberId, Pageable pageable);

    /**
     * approveWishBook - 희망 도서 신청 승인 (관리자 전용)
     *
     * 신청 중(REQUESTED) 상태를 구입 결정(APPROVED)으로 변경합니다.
     *
     * @param id 승인할 희망 도서 신청 ID
     * @throws RuntimeException 신청 기록 없음, 이미 처리된 경우
     */
    void approveWishBook(Long id);

    /**
     * rejectWishBook - 희망 도서 신청 반려 (관리자 전용)
     *
     * 신청 중(REQUESTED) 상태를 반려(REJECTED)로 변경합니다.
     *
     * @param id 반려할 희망 도서 신청 ID
     * @throws RuntimeException 신청 기록 없음, 이미 처리된 경우
     */
    void rejectWishBook(Long id);
}

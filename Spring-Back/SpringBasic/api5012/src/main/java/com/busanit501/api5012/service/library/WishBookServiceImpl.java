package com.busanit501.api5012.service.library;

import com.busanit501.api5012.domain.library.Member;
import com.busanit501.api5012.domain.library.WishBook;
import com.busanit501.api5012.dto.library.WishBookDTO;
import com.busanit501.api5012.repository.library.MemberRepository;
import com.busanit501.api5012.repository.library.WishBookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * WishBookServiceImpl - 희망 도서 신청 서비스 구현체
 *
 * WishBookService 인터페이스의 구현 클래스입니다.
 * 희망 도서 신청 등록, 목록 조회, 관리자 승인/반려 기능을 구현합니다.
 *
 * [중복 신청 방지]
 * 같은 회원이 동일한 도서 제목으로 이미 신청한 경우 중복 신청을 거부합니다.
 * WishBookRepository.existsByMemberIdAndWishBookTitle() 로 검증합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishBookServiceImpl implements WishBookService {

    /** WishBookRepository - 희망 도서 엔티티에 대한 DB 접근 */
    private final WishBookRepository wishBookRepository;

    /** MemberRepository - 회원 엔티티에 대한 DB 접근 */
    private final MemberRepository memberRepository;

    /**
     * createWishBook - 희망 도서 신청 등록
     *
     * [처리 순서]
     * 1. 회원 조회
     * 2. 동일 제목 중복 신청 확인
     * 3. WishBook 엔티티 생성 (상태: REQUESTED)
     */
    @Override
    @Transactional
    public Long createWishBook(WishBookDTO dto, Long memberId) {
        log.info("희망 도서 신청 시작 - memberId: {}, 도서명: {}", memberId, dto.getWishBookTitle());

        // 1단계: 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다. id: " + memberId));

        // 2단계: 동일 제목 중복 신청 확인
        boolean isDuplicate = wishBookRepository.existsByMemberIdAndWishBookTitle(
                memberId, dto.getWishBookTitle());
        if (isDuplicate) {
            throw new IllegalStateException(
                    "이미 신청한 도서입니다: " + dto.getWishBookTitle());
        }

        // 3단계: WishBook 엔티티 생성 (빌더 패턴)
        WishBook wishBook = WishBook.builder()
                .member(member)
                .wishBookTitle(dto.getWishBookTitle())
                .wishAuthor(dto.getWishAuthor())
                .wishPublisher(dto.getWishPublisher())
                .reason(dto.getReason())
                .build(); // status 기본값: "REQUESTED", regDate 기본값: now()

        Long savedId = wishBookRepository.save(wishBook).getId();
        log.info("희망 도서 신청 완료 - wishBookId: {}, 도서명: {}", savedId, dto.getWishBookTitle());

        return savedId;
    }

    /**
     * getMyWishBooks - 내 희망 도서 신청 목록 조회
     *
     * 마이페이지에서 내가 신청한 희망 도서 목록을 조회합니다.
     */
    @Override
    public Page<WishBookDTO> getMyWishBooks(Long memberId, Pageable pageable) {
        log.info("내 희망 도서 목록 조회 - memberId: {}, page: {}", memberId, pageable.getPageNumber());

        Page<WishBook> wishBookPage = wishBookRepository.findByMemberId(memberId, pageable);
        return wishBookPage.map(WishBookDTO::fromEntity);
    }

    /**
     * approveWishBook - 희망 도서 신청 승인 (관리자 전용)
     *
     * WishBook.approve() 도메인 메서드로 상태를 APPROVED 로 변경합니다.
     */
    @Override
    @Transactional
    public void approveWishBook(Long id) {
        log.info("희망 도서 승인 시작 - wishBookId: {}", id);

        WishBook wishBook = wishBookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "희망 도서 신청을 찾을 수 없습니다. id: " + id));

        // 신청 중(REQUESTED) 상태만 처리 가능
        if (!wishBook.isPending()) {
            throw new IllegalStateException(
                    "신청 중인 건만 승인할 수 있습니다. 현재 상태: " + wishBook.getStatus());
        }

        // 도메인 메서드로 상태 변경 → APPROVED
        wishBook.approve();
        log.info("희망 도서 승인 완료 - wishBookId: {}", id);
    }

    /**
     * rejectWishBook - 희망 도서 신청 반려 (관리자 전용)
     *
     * WishBook.reject() 도메인 메서드로 상태를 REJECTED 로 변경합니다.
     */
    @Override
    @Transactional
    public void rejectWishBook(Long id) {
        log.info("희망 도서 반려 시작 - wishBookId: {}", id);

        WishBook wishBook = wishBookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "희망 도서 신청을 찾을 수 없습니다. id: " + id));

        // 신청 중(REQUESTED) 상태만 반려 가능
        if (!wishBook.isPending()) {
            throw new IllegalStateException(
                    "신청 중인 건만 반려할 수 있습니다. 현재 상태: " + wishBook.getStatus());
        }

        // 도메인 메서드로 상태 변경 → REJECTED
        wishBook.reject();
        log.info("희망 도서 반려 완료 - wishBookId: {}", id);
    }
}

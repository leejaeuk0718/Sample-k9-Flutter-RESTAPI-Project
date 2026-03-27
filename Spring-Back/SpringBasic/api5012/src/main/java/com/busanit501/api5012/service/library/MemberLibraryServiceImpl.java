package com.busanit501.api5012.service.library;

import com.busanit501.api5012.domain.library.Member;
import com.busanit501.api5012.domain.library.MemberRole;
import com.busanit501.api5012.dto.library.MemberDTO;
import com.busanit501.api5012.dto.library.MemberSignupDTO;
import com.busanit501.api5012.repository.library.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * MemberLibraryServiceImpl - 도서관 회원 서비스 구현체
 *
 * MemberLibraryService 인터페이스의 구현 클래스입니다.
 * 회원가입, 회원정보 조회/수정, 중복 확인 등의 비즈니스 로직을 처리합니다.
 *
 * [주요 어노테이션 설명]
 * @Service       : 이 클래스가 서비스 레이어 빈임을 스프링에 등록합니다.
 * @RequiredArgsConstructor : final 필드에 대한 생성자를 자동 생성합니다. (의존성 주입)
 * @Slf4j         : log 변수를 자동 생성하여 로그를 출력할 수 있게 합니다.
 * @Transactional : DB 트랜잭션을 관리합니다.
 *   - readOnly=true  : SELECT 전용 트랜잭션 (성능 최적화, 실수 방지)
 *   - readOnly=false : INSERT/UPDATE/DELETE 가 가능한 일반 트랜잭션 (기본값)
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본: 읽기 전용 (변경 메서드에서 개별 @Transactional 사용)
public class MemberLibraryServiceImpl implements MemberLibraryService {

    /** MemberRepository - 회원 엔티티에 대한 DB 접근 */
    private final MemberRepository memberRepository;

    /**
     * PasswordEncoder - BCrypt 비밀번호 암호화
     * CustomSecurityConfig 에서 @Bean 으로 등록된 BCryptPasswordEncoder 를 주입합니다.
     * BCrypt 는 단방향 해시 알고리즘으로, 원문 복원이 불가능합니다.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * signup - 회원가입 처리
     *
     * [처리 순서]
     * 1. 아이디 중복 체크 → 중복이면 예외 발생
     * 2. 이메일 중복 체크 → 중복이면 예외 발생
     * 3. 비밀번호 확인 검증 (mpw == mpwConfirm)
     * 4. BCrypt 로 비밀번호 암호화
     * 5. Member 엔티티 빌더로 생성
     * 6. DB 저장 후 생성된 ID 반환
     */
    @Override
    @Transactional // 쓰기 트랜잭션 활성화
    public Long signup(MemberSignupDTO dto) {
        log.info("회원가입 처리 시작 - mid: {}", dto.getMid());

        // 1단계: 아이디 중복 확인
        if (memberRepository.existsByMid(dto.getMid())) {
            log.warn("아이디 중복 발생 - mid: {}", dto.getMid());
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다: " + dto.getMid());
        }

        // 2단계: 이메일 중복 확인
        if (memberRepository.existsByEmail(dto.getEmail())) {
            log.warn("이메일 중복 발생 - email: {}", dto.getEmail());
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다: " + dto.getEmail());
        }

        // 3단계: 비밀번호 확인 검증
        if (!dto.getMpw().equals(dto.getMpwConfirm())) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        // 4단계: BCrypt 암호화 (절대로 평문을 DB에 저장하지 않습니다)
        String encodedPassword = passwordEncoder.encode(dto.getMpw());
        log.debug("비밀번호 BCrypt 암호화 완료");

        // 5단계: Member 엔티티 생성 (@Builder 패턴 사용)
        Member member = Member.builder()
                .mid(dto.getMid())
                .mpw(encodedPassword)       // 암호화된 비밀번호 저장
                .mname(dto.getMname())
                .email(dto.getEmail())
                .region(dto.getRegion())
                .role(MemberRole.USER)       // 기본 권한: 일반 회원
                .build();

        // 6단계: DB 저장 후 생성된 기본키(id) 반환
        Long savedId = memberRepository.save(member).getId();
        log.info("회원가입 완료 - memberId: {}, mid: {}", savedId, dto.getMid());

        return savedId;
    }

    /**
     * getMemberByMid - 아이디로 회원 정보 조회
     *
     * Optional.orElseThrow() 를 사용하여 회원이 없으면 예외를 발생시킵니다.
     * 트랜잭션이 readOnly=true 이므로 DB 변경이 불가능합니다.
     */
    @Override
    public MemberDTO getMemberByMid(String mid) {
        log.info("회원 정보 조회 - mid: {}", mid);

        // Optional.orElseThrow() : 값이 없으면 람다로 지정한 예외를 발생시킵니다.
        Member member = memberRepository.findByMid(mid)
                .orElseThrow(() -> new RuntimeException("해당 아이디의 회원을 찾을 수 없습니다: " + mid));

        // MemberDTO.fromEntity() 정적 팩토리 메서드를 사용하여 엔티티를 DTO 로 변환
        return MemberDTO.fromEntity(member);
    }

    /**
     * updateMember - 회원 정보 수정
     *
     * 엔티티의 비즈니스 메서드(changeEmail, changeRegion)를 통해 변경합니다.
     * @Transactional + 더티체킹(Dirty Checking) 으로 별도 save() 없이 자동 UPDATE 됩니다.
     *
     * [더티체킹(Dirty Checking) 설명]
     * 트랜잭션 내에서 조회한 엔티티를 수정하면, 트랜잭션 종료 시
     * 스프링이 변경된 필드를 감지하여 자동으로 UPDATE 쿼리를 실행합니다.
     * 명시적인 save() 호출이 없어도 됩니다.
     */
    @Override
    @Transactional
    public void updateMember(String mid, MemberDTO dto) {
        log.info("회원 정보 수정 시작 - mid: {}", mid);

        Member member = memberRepository.findByMid(mid)
                .orElseThrow(() -> new RuntimeException("해당 아이디의 회원을 찾을 수 없습니다: " + mid));

        // 도메인 비즈니스 메서드를 통한 필드 변경 (직접 필드 접근 대신 메서드 사용)
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            // 이메일 변경 시 중복 여부 확인 (현재 이메일과 같은 경우는 제외)
            if (!member.getEmail().equals(dto.getEmail())
                    && memberRepository.existsByEmail(dto.getEmail())) {
                throw new IllegalArgumentException("이미 사용 중인 이메일입니다: " + dto.getEmail());
            }
            member.changeEmail(dto.getEmail());
        }

        if (dto.getRegion() != null) {
            member.changeRegion(dto.getRegion());
        }

        // 더티체킹으로 자동 UPDATE (별도 save() 불필요)
        log.info("회원 정보 수정 완료 - mid: {}", mid);
    }

    /**
     * updateProfileImage - 프로필 이미지 변경
     *
     * 이미지 업로드 후 저장된 파일명을 DB에 업데이트합니다.
     */
    @Override
    @Transactional
    public void updateProfileImage(String mid, String profileImg) {
        log.info("프로필 이미지 변경 - mid: {}, 파일명: {}", mid, profileImg);

        Member member = memberRepository.findByMid(mid)
                .orElseThrow(() -> new RuntimeException("해당 아이디의 회원을 찾을 수 없습니다: " + mid));

        // 도메인 메서드로 이미지 파일명 변경
        member.changeProfileImg(profileImg);
        // 더티체킹으로 자동 저장됩니다.

        log.info("프로필 이미지 변경 완료 - mid: {}", mid);
    }

    /**
     * checkDuplicateMid - 아이디 중복 체크
     *
     * existsByMid() 는 COUNT 쿼리를 사용하므로 findByMid() 보다 가볍습니다.
     */
    @Override
    public boolean checkDuplicateMid(String mid) {
        boolean isDuplicate = memberRepository.existsByMid(mid);
        log.debug("아이디 중복 체크 - mid: {}, 중복여부: {}", mid, isDuplicate);
        return isDuplicate;
    }

    /**
     * checkDuplicateEmail - 이메일 중복 체크
     */
    @Override
    public boolean checkDuplicateEmail(String email) {
        boolean isDuplicate = memberRepository.existsByEmail(email);
        log.debug("이메일 중복 체크 - email: {}, 중복여부: {}", email, isDuplicate);
        return isDuplicate;
    }
}

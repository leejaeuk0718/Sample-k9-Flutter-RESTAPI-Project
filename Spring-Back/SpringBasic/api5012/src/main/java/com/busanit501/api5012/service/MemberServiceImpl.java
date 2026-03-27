package com.busanit501.api5012.service;

import com.busanit501.api5012.domain.APIUser;
import com.busanit501.api5012.dto.APIUserDTO;
import com.busanit501.api5012.repository.APlUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final PasswordEncoder passwordEncoder;


    private final APlUserRepository apiUserRepository;

    @Override
    public void joinMember(APIUser apiUser) {
        // 🔹 아이디 중복 확인
        if (apiUserRepository.existsByMid(apiUser.getMid())) {
            throw new RuntimeException("이미 사용 중인 아이디입니다.");
        }
        APIUser apiUser2 = APIUser.builder()
                .mid(apiUser.getMid())
                .mpw(passwordEncoder.encode(apiUser.getMpw())) // 비밀번호 암호화
                .build();
        apiUserRepository.save(apiUser2);
    }

    @Override
    public boolean checkMember(String mid) {
        boolean check = apiUserRepository.existsByMid(mid);
        return check;
    }
}

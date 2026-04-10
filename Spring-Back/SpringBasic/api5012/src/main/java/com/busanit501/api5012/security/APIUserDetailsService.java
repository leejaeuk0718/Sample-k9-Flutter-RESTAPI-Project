package com.busanit501.api5012.security;

import com.busanit501.api5012.domain.library.Member;
import com.busanit501.api5012.dto.APIUserDTO;
import com.busanit501.api5012.repository.library.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class APIUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByMid(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Cannot find user with username: " + username)
                );

        log.info("APIUserDetailsService - Found Member: {}", member.getMid());

        String roleStr = "ROLE_" + member.getRole().name();
        APIUserDTO dto = new APIUserDTO(
                member.getMid(),
                member.getMpw(),
                List.of(new SimpleGrantedAuthority(roleStr))
        );

        log.info("APIUserDetailsService - Created APIUserDTO: {}", dto);
        return dto;
    }
}

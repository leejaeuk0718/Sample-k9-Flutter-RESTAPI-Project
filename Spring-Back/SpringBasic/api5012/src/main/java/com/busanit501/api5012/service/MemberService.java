package com.busanit501.api5012.service;

import com.busanit501.api5012.domain.APIUser;
import com.busanit501.api5012.dto.APIUserDTO;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface MemberService {
     void joinMember(APIUser apiUser);
     boolean checkMember(String mid);

}

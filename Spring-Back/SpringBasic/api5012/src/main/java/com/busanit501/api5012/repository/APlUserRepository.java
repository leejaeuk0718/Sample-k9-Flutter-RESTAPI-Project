package com.busanit501.api5012.repository;

import com.busanit501.api5012.domain.APIUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface APlUserRepository extends JpaRepository<APIUser, String> {
    boolean existsByMid(String mid);
}


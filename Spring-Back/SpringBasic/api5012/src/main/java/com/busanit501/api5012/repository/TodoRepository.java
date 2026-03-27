package com.busanit501.api5012.repository;

import com.busanit501.api5012.domain.Todo;
import com.busanit501.api5012.repository.search.TodoSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long>, TodoSearch {
    // ✅ 커서 기반 페이징
    Page<Todo> findByTnoLessThan(Long cursor, Pageable pageable);
    // ✅ 가장 큰 tno 값을 찾는 쿼리
    @Query("SELECT MAX(t.tno) FROM Todo t")
    Optional<Long> findMaxTno();

    // ✅ 최신 데이터부터 조회할 수 있도록 LessThanEqual 추가
    Page<Todo> findByTnoLessThanEqual(Long cursor, Pageable pageable);


}
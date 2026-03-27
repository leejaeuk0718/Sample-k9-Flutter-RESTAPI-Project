package com.busanit501.api5012.repository;

import com.busanit501.api5012.domain.Todo;
import com.busanit501.api5012.dto.PageRequestDTO;
import com.busanit501.api5012.dto.CursorPageRequestDTO;
import com.busanit501.api5012.dto.TodoDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class TodoRepositoryTests {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void testInsert() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Todo todo = Todo.builder()
                    .title("Todo..." + i)
                    .dueDate(LocalDate.of(2022, (i % 12) + 1, (i % 28) + 1)) // 일(day) 값 수정
                    .writer("user" + (i % 10))
                    .complete(false)
                    .build();

            todoRepository.save(todo);
            log.info("Inserted: " + todo);
        });
    }
    @Test
    public void testSearch() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .from(LocalDate.of(2022, 10, 1))
                .to(LocalDate.of(2022, 12, 31))
                .build();

        Page<TodoDTO> result = todoRepository.list(pageRequestDTO);
        result.forEach(todoDTO -> log.info(todoDTO.toString()));
    }
    @Test
    public void testCursorPaginationWithFilters() {
        int size = 5; // 한 번에 가져올 데이터 개수
        Long cursor = null; // 최초 요청 시 null

        log.info("✅ [Test] 특정 조건(기간, 완료 여부, 키워드) + 커서 기반 페이지네이션 테스트");

        // ✅ PageRequestDTO2 생성 (검색 조건 포함)
        CursorPageRequestDTO pageRequestDTO = CursorPageRequestDTO.builder()
                .size(size)
                .cursor(cursor)
                .from(LocalDate.of(2022, 10, 1)) // 2022년 10월 1일부터
                .to(LocalDate.of(2022, 12, 31)) // 2022년 12월 31일까지
                .completed(false) // 미완료된 할 일만
                .keyword("Todo") // "회의"가 포함된 제목 검색
                .build();

        // ✅ 첫 페이지 조회
        Page<TodoDTO> firstPage = todoRepository.list2(pageRequestDTO);
        log.info("✅ [Test] 첫 페이지 결과:");
        firstPage.getContent().forEach(todoDTO -> log.info(todoDTO.toString()));

        // ✅ 다음 페이지 조회 (커서 적용)
        Long nextCursor = firstPage.getContent().isEmpty() ? null : firstPage.getContent().get(firstPage.getContent().size() - 1).getTno();
        if (nextCursor != null) {
            log.info("✅ [Test] 다음 페이지 요청 - size: {}, cursor: {}", size, nextCursor);

            pageRequestDTO.setCursor(nextCursor); // 새로운 커서 설정
            Page<TodoDTO> secondPage = todoRepository.list2(pageRequestDTO);
            log.info("✅ [Test] 다음 페이지 결과:");
            secondPage.getContent().forEach(todoDTO -> log.info(todoDTO.toString()));
        }
    }

}
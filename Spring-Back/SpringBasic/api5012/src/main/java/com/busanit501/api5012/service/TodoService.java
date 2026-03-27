package com.busanit501.api5012.service;

import com.busanit501.api5012.dto.*;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface TodoService {
    Long register(TodoDTO todoDTO);
    TodoDTO read(Long tno);
    PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO);
    CursorPageResponseDTO<TodoDTO> list2(CursorPageRequestDTO pageRequestDTO);
    Long getMaxTno();
    void remove(Long tno);
    void modify(TodoDTO todoDTO);
}

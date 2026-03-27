package com.busanit501.api5012.repository.search;

import com.busanit501.api5012.dto.PageRequestDTO;
import com.busanit501.api5012.dto.CursorPageRequestDTO;
import com.busanit501.api5012.dto.TodoDTO;
import org.springframework.data.domain.Page;

public interface TodoSearch {
    Page<TodoDTO> list(PageRequestDTO pageRequestDTO);
    Page<TodoDTO> list2(CursorPageRequestDTO pageRequestDTO);
}
package org.zerock.onmomProject.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.onmomProject.entity.FreeBoard;
import org.zerock.onmomProject.entity.ReviewBoard;

import java.util.List;

public interface SearchFreeBoardRepository {
    FreeBoard search1();

    Page<Object[]> FreeSearchPage(String type, String keyword, Pageable pageable);
}

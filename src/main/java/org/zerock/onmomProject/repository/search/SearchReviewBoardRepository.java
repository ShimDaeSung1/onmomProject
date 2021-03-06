package org.zerock.onmomProject.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.onmomProject.entity.ReviewBoard;

import java.util.List;

public interface SearchReviewBoardRepository {
    List<ReviewBoard> search1();

    Page<Object[]> searchPage(String area, String type, String keyword, Pageable pageable);
}

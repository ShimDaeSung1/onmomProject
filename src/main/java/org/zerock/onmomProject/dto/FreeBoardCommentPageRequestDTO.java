package org.zerock.onmomProject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class FreeBoardCommentPageRequestDTO {

    private int page; // 페이지번호
    private int size; // page, size 필요 없을수도 있음. 슬릭 슬라이더 사용시 지우면 됨 //

    public FreeBoardCommentPageRequestDTO(){
        this.page = 1;
        this.size = 10; //size는 메인페이지에서 6개씩 돌아가므로
    }

    public Pageable getPageable(Sort sort){
        return PageRequest.of(page-1, size, sort); // sort는 좋아요수-싫어요수, pk(최신)
    }

}

package org.zerock.onmomProject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.onmomProject.dto.*;
import org.zerock.onmomProject.entity.*;
import org.zerock.onmomProject.repository.ImageRepository;
import org.zerock.onmomProject.repository.ReviewBoardRepository;
import org.zerock.onmomProject.repository.ReviewCommentRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReviewBoardServiceImpl implements ReviewBoardService {

    private final ReviewBoardRepository reviewBoardRepository; //자동 주입 final

    private final ReviewCommentRepository reviewCommentRepository; // 댓글 관련 repository, 게시글 삭제 기능 추가할 때 추가됨

    private final ImageRepository imageRepository;

    @Transactional
    @Override
    public Long register(ReviewBoardDTO reviewBoardDTO) {

        log.info("Review : "+reviewBoardDTO);
        Map<String, Object> entityMap = dtoToEntity(reviewBoardDTO);
        ReviewBoard reviewBoard = (ReviewBoard) entityMap.get("reviewBoard");
        List<Image> imageList =  (List<Image>) entityMap.get("imgList");

        reviewBoardRepository.save(reviewBoard);

        imageList.forEach(image -> {
            imageRepository.save(image);
        });
        return reviewBoard.getReview_id();
    }

    // index 페이지 후기 게시판 미리보기에 대한 페이징처리
    @Override
    public ReviewPageResultDTO<ReviewBoardDTO, Object[]> getList(ReviewPageRequestDTO reviewPageRequestDTO) {
        log.info(reviewPageRequestDTO);

        Pageable pageable = reviewPageRequestDTO.getPageable(Sort.by("like_cnt").descending());
//        Page<Object[]> result = reviewBoardRepository.getListPage(pageable);
        log.info("====================");

//        result.getContent().forEach(arr -> {
//            log.info(Arrays.toString(arr));
//        });

        Function<Object[], ReviewBoardDTO> fn = (arr -> entitiesToDTO(
                (ReviewBoard) arr[0],
                (List<Image>)(Arrays.asList((Image)arr[1]))
                )
        );

//        log.info("Result~!" + new ReviewPageResultDTO<>(result, fn));
//        return new ReviewPageResultDTO<>(result, fn);

        Page<Object[]> result = reviewBoardRepository.searchPage(
                reviewPageRequestDTO.getArea(),
                reviewPageRequestDTO.getType(),
                reviewPageRequestDTO.getKeyword(),
                reviewPageRequestDTO.getPageable(Sort.by("like_cnt").descending()));

        return new ReviewPageResultDTO<>(result, fn);
    }

    // review/list 페이지에 대한 페이징 처리
    @Override
    public ReviewPageResultDTO<ReviewBoardDTO, Object[]> getListReview(ReviewPageRequestDTO reviewPageRequestDTO){
        log.info(reviewPageRequestDTO);

        Pageable pageable = reviewPageRequestDTO.getPageable(Sort.by("review_id").descending());
        log.info("============");

        Function<Object[], ReviewBoardDTO> fn = (arr -> entitiesToDTO(
                (ReviewBoard) arr[0],
                (List<Image>) (Arrays.asList((Image)arr[1]))
                )
        );

        Page<Object[]> result = reviewBoardRepository.searchPage(
                reviewPageRequestDTO.getArea(),
                reviewPageRequestDTO.getType(),
                reviewPageRequestDTO.getKeyword(),
                reviewPageRequestDTO.getPageable(Sort.by("review_id").descending()));

        return new ReviewPageResultDTO<>(result, fn);
    }

    @Override
    public ReviewBoardDTO get(Long review_id) {
        List<Object[]> result = reviewBoardRepository.getReviewBoardWithAll(review_id);

        ReviewBoard reviewBoard = (ReviewBoard) result.get(0)[0];

        List<Image> imageList = new ArrayList<>();

        result.forEach(arr -> {
            Image image = (Image)arr[1];
            imageList.add(image);
        });
        return entitiesToDTO(reviewBoard, imageList);
    }


    @Transactional
    @Override
    public void removeWithReplies(Long review_id) { //삭제 기능 구현, 트렌젝션 추가
        // 댓글부터 삭제, review_id 값을 호출하여 이에 해당하는 모든 reviewComment 들을 삭제 처리해 준다
        reviewCommentRepository.deleteByReviewId(review_id);
        // 이미지부터 삭제, 위와 같은 원리
        imageRepository.delImgByReviewId(review_id);
        // 최종적으로 게시글을 삭제함
        reviewBoardRepository.deleteById(review_id);
    }

    @Transactional
    @Override
    public void modify(ReviewBoardDTO reviewBoardDTO) {
        ReviewBoard reviewBoard = reviewBoardRepository.getOne(reviewBoardDTO.getReview_id());
        log.info(reviewBoardDTO);

        reviewBoard.changeTitle(reviewBoardDTO.getTitle());
        reviewBoard.changeContent(reviewBoardDTO.getContent());

        reviewBoardRepository.save(reviewBoard);

    }
    //리뷰게시판 내가 쓴 글 페이징처리
    @Override
    public ReviewPageResultDTO<ReviewBoardDTO, Object[]> getMyPost(String member_id, ReviewPageRequestDTO reviewPageRequestDTO) {
        log.info(reviewPageRequestDTO);

        Function<Object[], ReviewBoardDTO> fn = (en ->
                entityToDTO((ReviewBoard) en[0], (Member) en[1], (Long) en[2]));

        Page<Object[]> result = reviewBoardRepository.getMyPostByMember_id(member_id,
                reviewPageRequestDTO.getPageable(Sort.by("review_id").descending()));

        return new ReviewPageResultDTO<>(result, fn);
    }

    @Override
    public Long updateLike(Long review_id){
        log.info(review_id);

        reviewBoardRepository.getUpdateReviewLikeCntByReview_id(review_id);

        return review_id;
    }

    @Override
    public Long updateHate(Long review_id){
        log.info(review_id);
        reviewBoardRepository.getUpdateReviewHateCntByReview_id(review_id);

        return review_id;
    }
}










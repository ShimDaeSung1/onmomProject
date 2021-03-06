package org.zerock.onmomProject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.onmomProject.entity.ReviewBoard;
import org.zerock.onmomProject.repository.search.SearchReviewBoardRepository;

import java.util.List;

public interface ReviewBoardRepository extends JpaRepository<ReviewBoard, Long>, SearchReviewBoardRepository {
    @Query("select rb, m from ReviewBoard rb LEFT JOIN rb.member m where rb.review_id =:review_id")
    Object getReviewBoardWithMember(@Param("review_id") Long review_id);

    @Query("SELECT rb, rbc FROM ReviewBoard rb LEFT JOIN ReviewBoardComment rbc ON rbc.reviewBoard = rb WHERE rb.review_id = :review_id")
    List<Object[]> getReviewBoardWithReviewBoardComment(@Param("review_id") Long review_id);

    @Query(value = "select rb, m_id, count(rbc) " +
            " from ReviewBoard rb" +
            " left join rb.member m_id" +
            " left join ReviewBoardComment rbc ON rbc.reviewBoard = rb " +
            " group by rb",
            countQuery = "select count(rb) from ReviewBoard rb")
    Page<Object[]> getReviewBoardWithReviewBoardCommentCount(Pageable pageable); //목록 화면에 필요한 데이터

//    reviewBoard를 불러올때, 작성자 이름과 댓글 수 불러오기
    @Query("select rb, m, count(rbc) " +
            " from ReviewBoard rb left join Member m on m.member_id = rb " +
            " left join ReviewBoardComment rbc on rbc.reviewBoard = rb" +
            " where rb.review_id = :review_id")
    Object getReviewBoardByReview_id(@Param("review_id") Long review_id);

    @Query("select rb, i " +
            " from ReviewBoard rb left outer join Image i on i.review = rb group by rb")
    Page<Object[]> getListPage(Pageable pageable);

    @Query("select rb, i " +
            " from ReviewBoard rb left join Image i on i.review = rb " +
            " where rb.review_id = :review_id")
    List<Object[]> getReviewBoardWithAll(@Param("review_id") Long review_id);

    // 멤버 아이디로 마이페이지 구현
    @Query(value = "select rb, m_id, count(rbc) " +
            " from ReviewBoard rb" +
            " left join rb.member m_id" +
            " left join ReviewBoardComment rbc ON rbc.reviewBoard = rb " +
            " where rb.member.member_id = :member_id " +
            " group by rb",
            countQuery = "select count(rb) from ReviewBoard rb where rb.member.member_id = :member_id")
    Page<Object[]> getMyPostByMember_id(@Param("member_id")String member_id, Pageable pageable);

    // 좋아요 싫어요 기능
    @Query(value = "UPDATE review_board SET like_cnt = like_cnt+1 WHERE review_board.review_id = :review_id", nativeQuery = true)
    Long getUpdateReviewLikeCntByReview_id(Long review_id);

    @Query(value = "UPDATE review_board SET hate_cnt = hate_cnt+1 WHERE review_board.review_id = :review_id", nativeQuery = true)
    Long getUpdateReviewHateCntByReview_id(Long review_id);


}

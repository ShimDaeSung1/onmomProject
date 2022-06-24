package org.zerock.onmomProject.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.onmomProject.entity.FreeBoard;
import org.zerock.onmomProject.entity.FreeBoardComment;
import org.zerock.onmomProject.entity.Member;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class FreeBoardCommentRepositoryTests {

    @Autowired
    private FreeBoardCommentRepository freeBoardCommentRepository;

    @Test
    public void insertReply(){
        IntStream.rangeClosed(1,100).forEach(i ->{

            Member member = Member.builder()
                    .member_id("u"+i)
                    .nickname("user"+i)
                    .pw("1111")
                    .build();

            FreeBoard freeBoard = FreeBoard.builder()
                    .content("content....." + i )
                    .hate_cnt((long)i) // 1부터 100사이 난수 발생
                    .like_cnt((long)i) // 1부터 100사이 난수 발생
                    .title("Title..." + i)
                    .member(member)
                    .build();

            FreeBoardComment freeBoardComment =FreeBoardComment.builder()
                    .content("Reply......." +i)
                    .board(freeBoard)
                    .member(member)
                    .build();
            freeBoardCommentRepository.save(freeBoardComment);
        });
    }

}

package org.zerock.onmomProject.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "member")
public class FreeBoard extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long free_id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(length = 20, nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String content;

    @Column(length = 20)
    private Long like_cnt;

    @Column(length = 20)
    private Long hate_cnt;

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeContent(String content){
        this.content = content;
    }

    @PrePersist
    public void prePersist(){
        this.like_cnt = this.like_cnt == null? 0:this.like_cnt;

        this.hate_cnt = this.hate_cnt == null? 0:this.hate_cnt;
    }
}

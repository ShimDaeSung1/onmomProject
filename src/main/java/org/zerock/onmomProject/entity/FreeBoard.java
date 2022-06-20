package org.zerock.onmomProject.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class FreeBoard extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long free_id;

    @ManyToOne
    private Member member_id;

    private String title;

    private String content;

    private Long like_cnt;

    private Long hate_cnt;

}
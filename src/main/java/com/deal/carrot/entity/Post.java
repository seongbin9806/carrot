package com.deal.carrot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "게시글")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "postSequenceGenerator", sequenceName = "postSequence", allocationSize = 1)
    @Column(name = "게시글번호")
    private int postId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "학번")
    private Student student;

    @Column(name = "카테고리명")
    private String categoryName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "거래자")
    private Student traderStudent;

    @Column(name = "거래금액")
    private int amount;

    @Column(name = "물품명")
    private String postName;

    @Column(name = "물품설명")
    private String content;

    @Column(name = "즐겨찾기수")
    private int favoritesCount = 0;

    @Column(name = "거래여부")
    private char isDeal = 'N';

    private String dealStatus = "";

    @Column(name = "등록날짜")
    private LocalDateTime regDate;

    public Post(Student student, String categoryName, int amount, String postName, String content) {
        this.student = student;
        this.categoryName = categoryName;
        this.amount = amount;
        this.postName = postName;
        this.content = content;
        this.regDate = LocalDateTime.now(); // regDate는 자동으로 현재 시간으로 설정
    }

}

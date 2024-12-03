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

    @Column(name = "학번")
    private int studentNumber;

    @Column(name = "학과명")
    private int departmentName;

    @Column(name = "카테고리명")
    private int categoryName;

    @Column(name = "거래자")
    private int traderNumber;

    @Column(name = "거래금액")
    private int amount;

    @Column(name = "물품명")
    private String postName;

    @Column(name = "물품설명")
    private String content;

    @Column(name = "거래여부")
    private char isDeal = 'N';

    @Column(name = "등록날짜")
    private LocalDateTime regDate;
}

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
@Table(name = "쪽지")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "noteSequenceGenerator")
    @SequenceGenerator(name = "noteSequenceGenerator", sequenceName = "noteSequence", allocationSize = 1)
    @Column(name = "쪽지번호")
    private int noteId;

    @Column(name = "학번")
    private int studentNumber;

    @Column(name = "게시글번호")
    private int postId;

    @Column(name = "제목")
    private String noteTitle;

    @Column(name = "내용")
    private String noteContent;

    @Column(name = "쪽지일자")
    private LocalDateTime regDate;
}
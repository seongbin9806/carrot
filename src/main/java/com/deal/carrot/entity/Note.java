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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "학번")
    private Student studentNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "받는사람학번")
    private Student receiveStudentNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "게시글번호")
    private Post postId;

    @Column(name = "내용")
    private String noteContent;

    @Column(name = "쪽지일자")
    private LocalDateTime regDate;

    @Transient
    private String clsMyMessage = "";

    public Student getStudent() {
        return this.studentNumber;
    }

    public Post getPost() {
        return this.postId;
    }

    public Note(Student student, Student receiveStudent, Post post, String noteContent) {
        this.studentNumber = student;
        this.receiveStudentNumber = receiveStudent;
        this.postId = post;
        this.noteContent = noteContent;
        this.regDate = LocalDateTime.now(); // regDate는 자동으로 현재 시간으로 설정
    }
}
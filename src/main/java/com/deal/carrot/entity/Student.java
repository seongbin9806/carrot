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
@Table(name = "학생")
public class Student {
    @Id
    @Column(name = "학번")
    private int studentNumber;

    @Column(name = "학과명")
    private String departmentName;

    @Column(name = "비밀번호")
    private String password;

    @Column(name = "이름")
    private String name;

    @Column(name = "게시글등록수")
    private int postCnt;

    @Column(name = "가입일자")
    private LocalDateTime regDate;

    @Column(name = "탈퇴여부")
    private char isUse = 'N';
}

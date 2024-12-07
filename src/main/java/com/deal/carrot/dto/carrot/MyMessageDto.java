package com.deal.carrot.dto.carrot;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MyMessageDto {
    private int postId;
    private LocalDateTime regDate;
    private int studentNumber;
    private int receiveStudentNumber;
    private int noteId;

    private String noteContent = "";

    public MyMessageDto(int postId, LocalDateTime regDate, int studentNumber, int receiveStudentNumber, int noteId) {
        this.postId = postId;
        this.regDate = regDate;
        this.studentNumber = studentNumber;
        this.receiveStudentNumber = receiveStudentNumber;
        this.noteId = noteId;
    }
}
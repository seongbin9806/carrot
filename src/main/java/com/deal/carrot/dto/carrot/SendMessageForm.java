package com.deal.carrot.dto.carrot;

import com.deal.carrot.entity.Note;
import com.deal.carrot.entity.Post;
import com.deal.carrot.entity.Student;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SendMessageForm {
    private int studentNumber;
    private int receiveStudentNumber;
    private int postId;

    private Student student;
    private Student receiveStudent;
    private Post post;
    private String noteContent;

    public Note toEntity() {
        return new Note(student, receiveStudent, post, noteContent);
    }
}
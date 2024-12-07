package com.deal.carrot.dto.carrot;

import com.deal.carrot.entity.Post;
import com.deal.carrot.entity.Student;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MyMessageDetail {
    MyMessageDto myMessage;

    private Post post;
    private Student student;
    private Student ReceiveStudent;

    private int traderStudentNumber = 0;
    private String traderName = "";
}
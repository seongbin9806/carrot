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
public class FinishDealForm {
    private int receiveStudentNumber;
    private int postId;
}
package com.deal.carrot.dto.carrot;

import com.deal.carrot.entity.Post;
import com.deal.carrot.entity.Student;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreatePostForm {

    private Student student;
    private String categoryName;
    private int amount;
    private String postName;
    private String content;

    public Post toEntity() {
        return new Post(student, categoryName, amount, postName, content);
    }

}
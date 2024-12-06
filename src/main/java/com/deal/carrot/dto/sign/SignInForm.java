package com.deal.carrot.dto.sign;

import com.deal.carrot.entity.Student;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignInForm {

    private int studentNumber;
    private String password;
}

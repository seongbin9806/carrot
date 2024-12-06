package com.deal.carrot.dto.sign;

import com.deal.carrot.entity.Student;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignUpForm {

    private int studentNumber;
    private String departmentName;
    private String password;
    private String name;

    public Student toEntity() {
        return new Student(studentNumber, departmentName, password, name);
    }
}

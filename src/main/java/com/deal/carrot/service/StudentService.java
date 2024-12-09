package com.deal.carrot.service;

import com.deal.carrot.dto.ResponseDTO;
import com.deal.carrot.dto.sign.SignInForm;
import com.deal.carrot.dto.sign.SignUpForm;
import com.deal.carrot.entity.Post;
import com.deal.carrot.entity.Student;
import com.deal.carrot.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;


@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public Student getStudentInfo(int studentNumber) {
        return studentRepository.findById(studentNumber)
                .orElseThrow(() -> new RuntimeException("학생을 찾을 수 없습니다."));
    }

    @Transactional
    public ResponseDTO signIn(SignInForm form) {
        try {
            int studentNumber = form.getStudentNumber();
            String password = form.getPassword();

            Optional<Student> optionalStudent = studentRepository.findById(studentNumber);
            if (optionalStudent.isPresent()) {
                Student student = optionalStudent.get();

                if (student.getIsUse() == 'Y') {
                    return new ResponseDTO(false, "탈퇴 처리된 학번입니다.");
                }

                // 저장된 비밀번호와 입력된 비밀번호 비교
                if (!Objects.equals(password, student.getPassword())) {
                    return new ResponseDTO(false, "비밀번호가 일치하지 않습니다.");
                }

                return new ResponseDTO(true, "로그인 성공");
            } else {
                return new ResponseDTO(false, "해당 학번을 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseDTO(false, "로그인 실패");
        }
    }

    @Transactional
    public ResponseDTO signUp(SignUpForm form) {
        try {
            int studentNumber = form.getStudentNumber();

            if (studentRepository.findById(studentNumber).isPresent())
                return new ResponseDTO(false, "이미 가입된 학번입니다.");


            Student student = form.toEntity(); // DTO -> Entity 변환
            studentRepository.save(student);   // 회원 저장

            return new ResponseDTO(true, "회원가입 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseDTO(false, "회원가입 실패");
        }
    }

    @Transactional
    public ResponseDTO deleteUser(int studentNumber) {
        try {
            Student student = this.getStudentInfo(studentNumber);

            student.setIsUse('Y');
            // 변경사항 저장
            studentRepository.save(student);

            return new ResponseDTO(true, "회원탈퇴 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseDTO(false, "회원탈퇴 실패");
        }
    }
}
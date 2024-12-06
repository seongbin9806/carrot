package com.deal.carrot.controller;

import com.deal.carrot.dto.ResponseDTO;
import com.deal.carrot.dto.sign.SignInForm;
import com.deal.carrot.dto.sign.SignUpForm;
import com.deal.carrot.service.DepartmentService;
import com.deal.carrot.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/signIn")
    public String signIn(Model model, HttpSession session) {
        Object studentNumberObj = session.getAttribute("studentNumber");

        if (studentNumberObj != null) {
            return "redirect:/";
        }

        return "sign/signIn";
    }

    @GetMapping("/signUp")
    public String signUp(Model model, HttpSession session) {
        Object studentNumberObj = session.getAttribute("studentNumber");

        if (studentNumberObj != null) {
            return "redirect:/";
        }

        model.addAttribute("departmentList", departmentService.getAllDepartmentList().getData());

        return "sign/signUp";
    }

    @PostMapping("/sign/signIn")
    public ResponseEntity<ResponseDTO> goLogin(SignInForm form, HttpSession session) {
        ResponseDTO response = studentService.signIn(form);


        // ResponseEntity를 사용하여 상태 코드와 함께 반환
        if (response.isResult()) {
            int studentNumber = form.getStudentNumber();
            session.setAttribute("studentNumber", studentNumber);

            return ResponseEntity.ok(response); // HTTP 200 OK
        } else {
            return ResponseEntity.status(403).body(response); // HTTP 500 Internal Server Error
        }
    }

    @PostMapping("/sign/signUp")
    public ResponseEntity<ResponseDTO> goSignUp(SignUpForm form, HttpSession session) {
        ResponseDTO response = studentService.signUp(form);

        // ResponseEntity를 사용하여 상태 코드와 함께 반환
        if (response.isResult()) {
            int studentNumber = form.getStudentNumber();
            session.setAttribute("studentNumber", studentNumber);

            return ResponseEntity.ok(response); // HTTP 200 OK
        } else {
            return ResponseEntity.status(403).body(response); // HTTP 500 Internal Server Error
        }
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // 세션 무효화
        return "redirect:/signIn";  // 로그인 페이지로 리다이렉트
    }
}
package com.deal.carrot.controller;

import com.deal.carrot.dto.ResponseDTO;
import com.deal.carrot.service.CategoryService;
import com.deal.carrot.service.DepartmentService;
import jakarta.servlet.http.HttpSession;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DepartmentService departmentService;


    @GetMapping("/admin/category")
    public String adminCategory(Model model) {

        model.addAttribute("categoryList", categoryService.getAllCategoryList().getData());

        return "admin/category";
    }

    @GetMapping("/admin/department")
    public String adminDepartment(Model model) {

        model.addAttribute("departmentList", departmentService.getAllDepartmentList().getData());

        return "admin/department";
    }

    @PostMapping("/admin/addCategory")
    public ResponseEntity<ResponseDTO> addCategory(String categoryName) {
        try {
            ResponseDTO response = categoryService.addCategory(categoryName);
            return ResponseEntity.ok(response); // HTTP 200 OK
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ResponseDTO(false, "정보를 가져오는 중 문제가 발생하였습니다."));
        }
    }

    @PostMapping("/admin/addDepartment")
    public ResponseEntity<ResponseDTO> addDepartment(String departmentName) {
        try {
            ResponseDTO response = departmentService.addDepartment(departmentName);
            return ResponseEntity.ok(response); // HTTP 200 OK
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ResponseDTO(false, "정보를 가져오는 중 문제가 발생하였습니다."));
        }
    }

}
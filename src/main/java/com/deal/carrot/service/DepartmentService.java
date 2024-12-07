package com.deal.carrot.service;

import com.deal.carrot.dto.ResponseDTO;
import com.deal.carrot.entity.Department;
import com.deal.carrot.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public ResponseDTO addDepartment(String departmentName) {
        try {
            if (departmentRepository.findById(departmentName).isPresent())
                return new ResponseDTO(false, "이미 등록된 학과입니다.");

            Department department = new Department();
            department.setDepartmentName(departmentName);

            departmentRepository.save(department);

            return new ResponseDTO(true, "학과 등록 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseDTO(false, "학과 등록 실패");
        }
    }

    @Transactional
    public ResponseDTO deleteDepartment(String departmentName) {
        try {
            departmentRepository.deleteById(departmentName);

            return new ResponseDTO(true, "학과 삭제 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseDTO(false, "학과 삭제 실패");
        }
    }

    @Transactional
    public List<Department> getAllDepartmentList() {
        List<Department> departmentList = departmentRepository.findAll(Sort.by(Sort.Order.asc("departmentName")));

        return departmentList;
    }
}
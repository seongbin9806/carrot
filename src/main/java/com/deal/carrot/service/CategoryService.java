package com.deal.carrot.service;

import com.deal.carrot.dto.ResponseDTO;
import com.deal.carrot.entity.Category;
import com.deal.carrot.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public ResponseDTO addCategory(String categoryName) {
        try {
            if (categoryRepository.findById(categoryName).isPresent())
                return new ResponseDTO(false, "이미 등록된 카테고리입니다.");

            Category category = new Category();
            category.setCategoryName(categoryName);

            categoryRepository.save(category);

            return new ResponseDTO(true, "카테고리 등록 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseDTO(false, "카테고리 등록 실패");
        }
    }

    @Transactional
    public ResponseDTO deleteCategory(String categoryName) {
        try {
            categoryRepository.deleteById(categoryName);

            return new ResponseDTO(true, "카테고리 삭제 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseDTO(false, "카테고리 삭제 실패");
        }
    }

    @Transactional
    public List<Category> getAllCategoryList() {
        List<Category> categoryList = categoryRepository.findAll(Sort.by(Sort.Order.asc("categoryName")));

        return categoryList;
    }
}
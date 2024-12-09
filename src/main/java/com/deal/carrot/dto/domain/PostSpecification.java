package com.deal.carrot.dto.domain;

import com.deal.carrot.entity.Post;
import com.deal.carrot.entity.Student;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class PostSpecification {

    public static Specification<Post> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + keyword + "%";
            return criteriaBuilder.like(root.get("postName"), likePattern);
        };
    }

    public static Specification<Post> hasCategory(String categoryName) {
        return (root, query, criteriaBuilder) -> {
            if (categoryName == null || categoryName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("categoryName"), categoryName);
        };
    }

    public static Specification<Post> hasDepartment(String departmentName) {
        return (root, query, criteriaBuilder) -> {
            if (departmentName == null || departmentName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            Join<Post, Student> studentJoin = root.join("student");
            return criteriaBuilder.equal(studentJoin.get("departmentName"), departmentName);
        };
    }

    public static Specification<Post> isUseY() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isUse"), "Y");
    }
}

package com.deal.carrot.service;

import com.deal.carrot.dto.ResponseDTO;
import com.deal.carrot.dto.carrot.CreatePostForm;
import com.deal.carrot.entity.*;
import com.deal.carrot.repository.PostRepository;
import com.deal.carrot.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public ResponseDTO createPost(CreatePostForm form, int studentNumber) {

        Student student = studentService.getStudentInfo(studentNumber);
        form.setStudent(student);

        Post post = form.toEntity(); // DTO -> Entity 변환
        postRepository.save(post);   // 회원 저장

        return new ResponseDTO(true, "게시글 작성 성공");
    }

    @Transactional
    public List<Post> getPostList() {
        List<Post> postList = postRepository.findAll(Sort.by(Sort.Order.desc("regDate")));

        return postList;
    }

    @Transactional
    public Post getPostDetail(int postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
    }
}

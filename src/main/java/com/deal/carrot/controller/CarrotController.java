package com.deal.carrot.controller;

import com.deal.carrot.dto.ResponseDTO;
import com.deal.carrot.dto.carrot.CreatePostForm;
import com.deal.carrot.entity.Favorites;
import com.deal.carrot.entity.Post;
import com.deal.carrot.entity.Student;
import com.deal.carrot.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
public class CarrotController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private PostService postService;

    @Autowired
    private FavoritesService favoritesService;

    @GetMapping("/")
    public String itemList(Model model, HttpSession session) {
        Object studentNumberObj = session.getAttribute("studentNumber");

        if (studentNumberObj == null) {
            return "redirect:/signIn";
        }

        List<Post> postList = postService.getPostList();

        for (Post post : postList) {
            String dealStatus = (post.getIsDeal() == 'N') ? "거래 가능" : "거래 완료";
            post.setDealStatus(dealStatus); // dealStatus 필드를 Post 객체에 추가
        }

        model.addAttribute("categoryList", categoryService.getAllCategoryList().getData());
        model.addAttribute("departmentList", departmentService.getAllDepartmentList().getData());
        model.addAttribute("postList", postList);

        return "carrot/itemList";
    }

    @GetMapping("/createItem")
    public String createItemView(Model model, HttpSession session) {
        Object studentNumberObj = session.getAttribute("studentNumber");

        if (studentNumberObj == null) {
            return "redirect:/signIn";
        }

        model.addAttribute("categoryList", categoryService.getAllCategoryList().getData());

        return "carrot/createItem";
    }

    @PostMapping("/createPost")
    public ResponseEntity<ResponseDTO> createPost(CreatePostForm form, HttpSession session) {
        int studentNumber = (int) session.getAttribute("studentNumber");

        ResponseDTO response = postService.createPost(form, studentNumber);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/itemDetails")
    public String itemDetails(@RequestParam("postId") int postId, Model model, HttpSession session) {
        int studentNumber = (int) session.getAttribute("studentNumber");
        Post postInfo = postService.getPostDetail(postId);

        /* 즐겨찾기 여부 체크 */
        Post post = postService.getPostDetail(postId);
        Student student = studentService.getStudentInfo(studentNumber);

        Favorites favorites = favoritesService.getFavoriteInfo(post, student);
        boolean isFavorites = favorites != null;

        model.addAttribute("postInfo", postInfo);
        model.addAttribute("favoritesText", isFavorites? "즐겨찾기 삭제" : "즐겨찾기 등록");

        return "carrot/itemDetails";
    }

    @PostMapping("/favoritePost")
    public ResponseEntity<ResponseDTO> favoritePost(int postId, HttpSession session) {
        int studentNumber = (int) session.getAttribute("studentNumber");

        ResponseDTO response = favoritesService.favoritePost(postId, studentNumber);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/favoritesList")
    public String favoritesList(Model model) {

        return "carrot/favoritesList";
    }

    @GetMapping("/messageList")
    public String messageList(Model model) {

        return "carrot/messageList";
    }

    @GetMapping("/sendMessage")
    public String sendMessage(Model model) {

        return "carrot/sendMessage";
    }
}

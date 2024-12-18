package com.deal.carrot.controller;

import com.deal.carrot.dto.ResponseDTO;
import com.deal.carrot.dto.carrot.*;
import com.deal.carrot.entity.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Autowired
    private NoteService noteService;

    @Autowired
    private OracleProcedureService oracleProcedureService;

    @GetMapping("/")
    public String itemList(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                           @RequestParam(value = "categoryName", required = false, defaultValue = "") String categoryName,
                           @RequestParam(value = "departmentName", required = false, defaultValue = "") String departmentName,
                           Model model, HttpSession session) {
        Object studentNumberObj = session.getAttribute("studentNumber");

        if (studentNumberObj == null) {
            return "redirect:/signIn";
        }

        List<Post> postList = postService.getPostList(keyword, categoryName, departmentName);

        for (Post post : postList) {
            post.setFormatRegDate(oracleProcedureService.callPostDateProcedure(post.getPostId()));
            post.getStudent().setGrade(oracleProcedureService.callStudentGradeProcedure(post.getStudent().getStudentNumber()));

            String dealStatus = (post.getIsDeal() == 'N') ? "거래 가능" : "거래 완료";
            post.setDealStatus(dealStatus); // dealStatus 필드를 Post 객체에 추가
        }

        List<Category> categoryList = categoryService.getAllCategoryList();
        List<Department> departmentList = departmentService.getAllDepartmentList();

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("departmentList", departmentList);
        model.addAttribute("postList", postList);

        // 선택 상태 설정
        categoryList.forEach(category -> category.setSelected(category.getCategoryName().equals(categoryName)));
        departmentList.forEach(department -> department.setSelected(department.getDepartmentName().equals(departmentName)));

        /* getParam */
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("departmentName", departmentName);
        return "carrot/itemList";
    }

    @GetMapping("/createItem")
    public String createItemView(Model model, HttpSession session) {
        Object studentNumberObj = session.getAttribute("studentNumber");

        if (studentNumberObj == null) {
            return "redirect:/signIn";
        }

        model.addAttribute("categoryList", categoryService.getAllCategoryList());

        return "carrot/createItem";
    }

    @PostMapping("/createPost")
    public ResponseEntity<ResponseDTO> createPost(CreatePostForm form, HttpSession session) {
        int studentNumber = (int) session.getAttribute("studentNumber");

        ResponseDTO response = postService.createPost(form, studentNumber);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/deletePost")
    public ResponseEntity<ResponseDTO> deletePost(int postId) {
        ResponseDTO response = postService.deletePost(postId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/deleteUser")
    public ResponseEntity<ResponseDTO> deleteUser(HttpSession session) {
        int studentNumber = (int) session.getAttribute("studentNumber");

        ResponseDTO response = studentService.deleteUser(studentNumber);
        session.invalidate();  // 세션 무효화

        return ResponseEntity.ok(response);
    }

    @GetMapping("/itemDetails")
    public String itemDetails(@RequestParam("postId") int postId, Model model, HttpSession session) {
        int studentNumber = (int) session.getAttribute("studentNumber");
        Post postInfo = postService.getPostDetail(postId);

        /* 나의 게시글인지 체크 */
        boolean isMyPost = (postInfo.getStudent().getStudentNumber() == studentNumber);

        /* 즐겨찾기 여부 체크 */
        Post post = postService.getPostDetail(postId);
        Student student = studentService.getStudentInfo(studentNumber);

        Favorites favorites = favoritesService.getFavoriteInfo(post, student);
        boolean isFavorites = favorites != null;

        postInfo.setFormatRegDate(oracleProcedureService.callPostDateProcedure(postInfo.getPostId()));
        postInfo.getStudent().setGrade(oracleProcedureService.callStudentGradeProcedure(postInfo.getStudent().getStudentNumber()));

        model.addAttribute("postInfo", postInfo);
        model.addAttribute("favoritesText", isFavorites ? "즐겨찾기 삭제" : "즐겨찾기 등록");
        model.addAttribute("isMyPost", isMyPost);

        return "carrot/itemDetails";
    }

    @PostMapping("/favoritePost")
    public ResponseEntity<ResponseDTO> favoritePost(int postId, HttpSession session) {
        int studentNumber = (int) session.getAttribute("studentNumber");

        ResponseDTO response = favoritesService.favoritePost(postId, studentNumber);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/favoritesList")
    public String favoritesList(Model model, HttpSession session) {
        int studentNumber = (int) session.getAttribute("studentNumber");

        List<Favorites> favoritesList = favoritesService.getFavoritesList(studentNumber);

        for (Favorites favorite : favoritesList) {

            favorite.getPost().setFormatRegDate(oracleProcedureService.callPostDateProcedure(favorite.getPost().getPostId()));
            favorite.getPost().getStudent().setGrade(oracleProcedureService.callStudentGradeProcedure(favorite.getPost().getStudent().getStudentNumber()));

            String dealStatus = (Objects.equals(favorite.getPost().getIsDeal(), 'N')) ? "거래 가능" : "거래 완료";
            favorite.getPost().setDealStatus(dealStatus); // dealStatus 필드를 Post 객체에 추가
        }

        model.addAttribute("favoritesList", favoritesList);

        return "carrot/favoritesList";
    }

    @GetMapping("/sendMessage")
    public String sendMessage(@RequestParam("postId") int postId,
                              @RequestParam("receiveStudentNumber") int receiveStudentNumber,
                              Model model, HttpSession session) {
        int studentNumber = (int) session.getAttribute("studentNumber");
        Post postInfo = postService.getPostDetail(postId);

        /* 나의 게시글이고 완료가 안 되었는지 체크 */
        boolean isShowFinish = (postInfo.getStudent().getStudentNumber() == studentNumber && postInfo.getIsDeal() == 'N');

        model.addAttribute("postId", postId);
        model.addAttribute("receiveStudentNumber", receiveStudentNumber);
        model.addAttribute("postInfo", postInfo);
        model.addAttribute("messageList", noteService.getMessageList(postId, studentNumber, receiveStudentNumber));
        model.addAttribute("isShowFinish", isShowFinish);

        return "carrot/sendMessage";
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<ResponseDTO> sendMessage(SendMessageForm form, HttpSession session) {
        int studentNumber = (int) session.getAttribute("studentNumber");

        form.setStudentNumber(studentNumber);
        ResponseDTO response = noteService.sendMessage(form);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/finishDeal")
    public ResponseEntity<ResponseDTO> finishDeal(FinishDealForm form, HttpSession session) {
        int studentNumber = (int) session.getAttribute("studentNumber");

        ResponseDTO response = postService.finishDeal(form);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/messageList")
    public String messageList(Model model, HttpSession session) {
        int studentNumber = (int) session.getAttribute("studentNumber");

        List<MyMessageDto> myMessageList = noteService.getMyMessageList(studentNumber);
        List<MyMessageDetail> myMessageDetailList = new ArrayList<>();

        for (MyMessageDto myMessage : myMessageList) {
            MyMessageDetail myMessageDetail = new MyMessageDetail();

            Note noteInfo = noteService.getLastMessage(myMessage.getNoteId());
            myMessage.setNoteContent(noteInfo.getNoteContent());
            myMessageDetail.setMyMessage(myMessage);

            myMessageDetail.setPost(postService.getPostDetail(myMessage.getPostId()));
            myMessageDetail.setStudent(studentService.getStudentInfo(myMessage.getStudentNumber()));
            myMessageDetail.setReceiveStudent(studentService.getStudentInfo(myMessage.getReceiveStudentNumber()));

            /* 거래자 정보 추출 */
            String traderName = "";
            int traderStudentNumber = 0;

            if (myMessage.getStudentNumber() == studentNumber) {
                traderName = myMessageDetail.getReceiveStudent().getName();
                traderStudentNumber = myMessageDetail.getReceiveStudent().getStudentNumber();
            } else {
                traderName = myMessageDetail.getStudent().getName();
                traderStudentNumber = myMessageDetail.getStudent().getStudentNumber();
            }
            myMessageDetail.setTraderName(traderName);
            myMessageDetail.setTraderStudentNumber(traderStudentNumber);

            myMessageDetailList.add(myMessageDetail);
        }

        log.info(myMessageDetailList.toString());
        model.addAttribute("messageList", myMessageDetailList);

        return "carrot/messageList";
    }
}

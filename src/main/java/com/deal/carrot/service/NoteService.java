package com.deal.carrot.service;

import com.deal.carrot.dto.carrot.MyMessageDto;
import com.deal.carrot.dto.ResponseDTO;
import com.deal.carrot.dto.carrot.SendMessageForm;
import com.deal.carrot.entity.Note;
import com.deal.carrot.entity.Post;
import com.deal.carrot.entity.Student;
import com.deal.carrot.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteService {
    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Autowired
    private PostService postService;

    @Autowired
    private StudentService studentService;
    
    @Transactional
    public List<MyMessageDto> getMyMessageList(int studentNumber) {
        return noteRepository.findNotesByStudentId(studentNumber);
    }

    @Transactional
    public List<Note> getMessageList(int postId, int studentNumber, int receiveStudentNumber) {
        Student student = studentService.getStudentInfo(studentNumber);
        Student receiveStudent = studentService.getStudentInfo(receiveStudentNumber);

        List<Note> messageList = noteRepository.findAllByPostIdAndStudentNumberOrReceiveStudentNumber(postId, student, receiveStudent);

        // 나의 메세지 구분
        messageList.forEach(message -> {
            boolean isMyMessage = message.getStudent().getStudentNumber() == studentNumber;
            message.setClsMyMessage(isMyMessage ? "sent" : "received");
        });

        return messageList;
    }

    @Transactional
    public ResponseDTO sendMessage(SendMessageForm form) {

        Student student = studentService.getStudentInfo(form.getStudentNumber());
        Post post = postService.getPostDetail(form.getPostId());
        Student recevieStudent = studentService.getStudentInfo(form.getReceiveStudentNumber());

        form.setStudent(student);
        form.setReceiveStudent(recevieStudent);

        form.setPost(post);

        Note note = form.toEntity(); // DTO -> Entity 변환
        noteRepository.save(note);

        return new ResponseDTO(true, "쪽지 보내기 성공");
    }

    @Transactional
    public Note getLastMessage(int noteId) {
        return noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("쪽지 내용을 찾을 수 없습니다."));
    }

    @Transactional
    public void deleteMessages(Post post) {
        noteRepository.deleteByPostId(post);
    }
}

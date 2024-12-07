package com.deal.carrot.repository;

import com.deal.carrot.entity.Favorites;
import com.deal.carrot.entity.Note;
import com.deal.carrot.entity.Post;
import com.deal.carrot.entity.Student;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {

    @Query("SELECT n FROM Note n WHERE n.postId.id = :postId AND " +
            "(n.studentNumber = :studentNumber OR n.receiveStudentNumber = :receiveStudentNumber) " +
            "ORDER BY n.regDate DESC")
    List<Note> findAllByPostIdAndStudentNumberOrReceiveStudentNumber(
            @Param("postId") int postId, // Post ID는 Long 타입
            @Param("studentNumber") Student studentNumber, // Student 객체
            @Param("receiveStudentNumber") Student receiveStudentNumber // Student 객체
    );
}


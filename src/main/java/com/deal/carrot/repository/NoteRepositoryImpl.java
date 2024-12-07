package com.deal.carrot.repository;

import com.deal.carrot.dto.carrot.MyMessageDto;
import com.deal.carrot.entity.Note;
import com.deal.carrot.entity.Post;
import com.deal.carrot.entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class NoteRepositoryImpl implements NoteRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MyMessageDto> findNotesByStudentId(int studentId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MyMessageDto> query = cb.createQuery(MyMessageDto.class);
        Root<Note> note = query.from(Note.class);

        // Join fields
        Join<Note, Post> post = note.join("postId", JoinType.LEFT);
        Join<Note, Student> student = note.join("studentNumber", JoinType.LEFT);
        Join<Note, Student> receiveStudent = note.join("receiveStudentNumber", JoinType.LEFT);

        // Create CASE WHEN for LEAST and GREATEST logic
        Expression<Object> studentId1 = cb.selectCase()
                .when(cb.lessThan(student.get("studentNumber"), receiveStudent.get("studentNumber")), student.get("studentNumber"))
                .otherwise(receiveStudent.get("studentNumber"));

        Expression<Object> studentId2 = cb.selectCase()
                .when(cb.greaterThan(student.get("studentNumber"), receiveStudent.get("studentNumber")), student.get("studentNumber"))
                .otherwise(receiveStudent.get("studentNumber"));

        // Select fields
        query.select(cb.construct(
                MyMessageDto.class,
                post.get("postId"),  // 게시글번호
                cb.max(note.get("regDate")),  // 최신 쪽지일자
                studentId1,  // 학번1
                studentId2,  // 학번2
                cb.max(note.get("noteId"))  // 쪽지번호
        ));

        // Where clause
        Predicate studentPredicate = cb.equal(student.get("studentNumber"), studentId);
        Predicate receiveStudentPredicate = cb.equal(receiveStudent.get("studentNumber"), studentId);
        query.where(cb.or(studentPredicate, receiveStudentPredicate));

        // Group by - case WHEN expression included in groupBy
        query.groupBy(
                post.get("postId"),
                studentId1,
                studentId2
        );

        // Order by - 최신 쪽지일자 기준 내림차순
        query.orderBy(cb.desc(cb.max(note.get("regDate"))));

        // Execute query
        return entityManager.createQuery(query).getResultList();
    }
}

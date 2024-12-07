package com.deal.carrot.repository;

import com.deal.carrot.dto.carrot.MyMessageDto;

import java.util.List;

public interface NoteRepositoryCustom {
    List<MyMessageDto> findNotesByStudentId(int studentId);
}
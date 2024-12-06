package com.deal.carrot.repository;

import com.deal.carrot.entity.Favorites;
import com.deal.carrot.entity.Post;
import com.deal.carrot.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorites, Integer> {
    Favorites findByPostIdAndStudentNumber(Post post, Student student);
}


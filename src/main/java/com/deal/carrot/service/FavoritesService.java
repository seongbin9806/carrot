package com.deal.carrot.service;

import com.deal.carrot.dto.ResponseDTO;
import com.deal.carrot.entity.Favorites;
import com.deal.carrot.entity.Post;
import com.deal.carrot.entity.Student;
import com.deal.carrot.repository.FavoritesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoritesService {
    private final FavoritesRepository favoritesRepository;

    @Autowired
    public FavoritesService(FavoritesRepository favoritesRepository) {
        this.favoritesRepository = favoritesRepository;
    }

    @Autowired
    private PostService postService;

    @Autowired
    private StudentService studentService;

    @Transactional
    public List<Favorites> getFavoritesList(int studentNumber) {
        Student student = studentService.getStudentInfo(studentNumber);

        return favoritesRepository.findAllByStudentNumber(student);
    }

    @Transactional
    public Favorites getFavoriteInfo(Post post, Student student) {
        return favoritesRepository.findByPostIdAndStudentNumber(post, student);
    }

    @Transactional
    public ResponseDTO favoritePost(int postId, int studentNumber) {
        try {
            Post post = postService.getPostDetail(postId);
            Student student = studentService.getStudentInfo(studentNumber);

            Favorites favorites = this.getFavoriteInfo(post, student);
            boolean isFavorites = favorites != null;

            if (isFavorites) {
                /* 삭제 */
                int favoritesId = favorites.getFavoritesId();
                favoritesRepository.deleteById(favoritesId);
            } else {
                /* 등록 */
                Favorites form = new Favorites();
                form.setPostId(post);
                form.setStudentNumber(student);
                favoritesRepository.save(form);
            }

            return new ResponseDTO(true, "즐겨찾기 제어 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseDTO(false, "즐겨찾기 제어 실패");
        }
    }

    @Transactional
    public void deleteFavorites(Post post) {
        favoritesRepository.deleteByPostId(post);
    }
}

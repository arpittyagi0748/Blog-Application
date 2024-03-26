package com.io.mountblue.blogapplication.dao;

import com.io.mountblue.blogapplication.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post,Integer> {

    Page<Post> findByIsPublished(boolean isPublished, Pageable pageable);

    List<Post> findByIsPublished(boolean isPublished);
    List<Post> findByIsPublishedOrderByPublishedAt(boolean isPublished);
    List<Post> findByIsPublishedOrderByTitleAsc(boolean isPublished);

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN FETCH p.tags t " +
            "WHERE p.isPublished = :isPublished " +
            "AND (LOWER(p.author.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Post> findByIsPublishedAndSearchQuery(@Param("isPublished") boolean isPublished, @Param("keyword") String keyword);
    @Query("SELECT DISTINCT p.author.name FROM Post p")
    List<String> findAllAuthors();

}


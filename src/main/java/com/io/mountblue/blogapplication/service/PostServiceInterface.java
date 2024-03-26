package com.io.mountblue.blogapplication.service;

import com.io.mountblue.blogapplication.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.List;

public interface PostServiceInterface {

    Post getPostById(int id);

    void deletePostById(int id);

    void savePost(Post post);

    void createPostWithTags(Post post, Model model, String action, String userTag);

    void updatePost(Model model, Post post);

    Page<Post> getAllPublishedPosts(int page, int size);

    List<Post> filterPostsByAuthorsTagsAndDate(List<Post> posts, List<String> authors, List<String> tags, LocalDate startDate, LocalDate endDate);
}

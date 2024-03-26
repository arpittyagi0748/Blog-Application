package com.io.mountblue.blogapplication.service;

import com.io.mountblue.blogapplication.dao.PostRepository;
import com.io.mountblue.blogapplication.dao.TagRepository;
import com.io.mountblue.blogapplication.dao.UserRepository;
import com.io.mountblue.blogapplication.model.Post;
import com.io.mountblue.blogapplication.model.Tag;
import com.io.mountblue.blogapplication.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService implements PostServiceInterface {
    private PostRepository postRepository;
    private UserRepository userRepository;
    private TagRepository tagRepository;
    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, TagRepository tagRepository){
        this.postRepository=postRepository;
        this.userRepository=userRepository;
        this.tagRepository=tagRepository;
    }
    public Post getPostById(int id){
        return  postRepository.findById(id).get();
    }
    public void deletePostById(int id){
        postRepository.deleteById(id);
    }
    public void savePost(Post post){
        postRepository.save(post);
    }

public void createPostWithTags(Post post, Model model, String action, String userTag) {
    LocalDateTime now = LocalDateTime.now();
    Optional<Post> optionalPost = postRepository.findById(post.getId());


    boolean postExistsAndPublished = optionalPost.isPresent() && optionalPost.get().isPublished();

    if (action.equals("Draft")) {
        post.setPublished(false);
    } else if (action.equals("Publish")) {
        post.setPublished(true);
        post.setPublishedAt(now);
    }


    if (optionalPost.isPresent()) {
        if (postExistsAndPublished) {
            post.setPublishedAt(optionalPost.get().getPublishedAt());
            post.setCreatedAt(optionalPost.get().getCreatedAt());
        }
        post.setUpdatedAt(now);
    } else {

        post.setCreatedAt(now);
    }
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    User user = userRepository.findByName(username);
    if (user == null) {
        model.addAttribute("error", "User not found");
        System.out.println("error");
    }

    post.setAuthor(user);
    String content = post.getContent();
    if (content != null && content.length() > 50) {
        post.setExcerpt(content.substring(0, 50) + "...");
    } else {
        post.setExcerpt(content);
    }

    List<Tag> newTags = new ArrayList<>();
    for (Tag tag : post.getTagList()) {
        Optional<Tag> tagName = tagRepository.findByName(tag.getName());
        if (!tagName.isEmpty()) {
            newTags.add(tagName.get());
        } else {
            newTags.add(tag);
        }
    }
    post.setTagsList(newTags);
}

    public void updatePost(Model model, Post post) {
        model.addAttribute("post", post);
    }


    public Page<Post> getAllPublishedPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByIsPublished(true, pageable);
    }

    public List<Post> filterPostsByAuthorsTagsAndDate(List<Post> posts, List<String> authors, List<String> tags, LocalDate startDate, LocalDate endDate) {
        return posts.stream()
                .filter(post -> (authors == null || authors.isEmpty() || authors.contains(post.getAuthor().getName())))
                .filter(post -> {
                    List<String> postTags = post.getTagList().stream().map(tag -> tag.getName()).collect(Collectors.toList());
                    return tags == null || tags.isEmpty() || postTags.stream().anyMatch(tags::contains);
                })
                .filter(post -> (startDate == null || endDate == null) ||
                        (post.getPublishedAt().toLocalDate().isEqual(startDate) || post.getPublishedAt().toLocalDate().isAfter(startDate)) &&
                                (post.getPublishedAt().toLocalDate().isEqual(endDate) || post.getPublishedAt().toLocalDate().isBefore(endDate.plusDays(1))))
                .collect(Collectors.toList());
    }


}

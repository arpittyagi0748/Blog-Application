package com.io.mountblue.blogapplication.controller;

import com.io.mountblue.blogapplication.dao.PostRepository;
import com.io.mountblue.blogapplication.dao.TagRepository;
import com.io.mountblue.blogapplication.dao.UserRepository;
import com.io.mountblue.blogapplication.model.Post;
import com.io.mountblue.blogapplication.model.User;
import com.io.mountblue.blogapplication.service.PostService;
import com.io.mountblue.blogapplication.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.io.mountblue.blogapplication.model.Tag;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.*;

import static org.yaml.snakeyaml.tokens.Token.ID.Tag;
@RestController
@RequestMapping("/myblog")
public class PostRestController {

    private final PostService postService;
    private final PostRepository postRepository;
    @Autowired
    private final TagRepository tagRepository;
    @Autowired
    private final UserRepository userRepository;
    private final UserService userService;
    @Autowired
    public PostRestController(PostService postService, PostRepository postRepository,TagRepository tagRepository,UserRepository userRepository,UserService userService) {
        this.postService = postService;
        this.postRepository = postRepository;
        this.tagRepository=tagRepository;
        this.userRepository=userRepository;
        this.userService=userService;
    }



    @PostMapping("/processPost")
    public String createPost(@RequestBody Post post) {
        postService.savePost(post);
        return "success";
    }

    @GetMapping("/post/{id}")
    public Post getPost(@PathVariable("id") int id) {
        Post post;
        post = postRepository.findById(id).orElse(null);

        return post;

    }

    @DeleteMapping("/deletePost/{id}")
    public String deletePost(@PathVariable("id") int id) {
        Post thepost = postService.getPostById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = authentication.getName();
        User user = userService.findByName(loggedInUser);
        if (authentication.getAuthorities().toString().equals("[ROLE_AUTHOR]") && !thepost.getAuthor().getName().equals(loggedInUser)) {
            return "access-denied";
        }
        postService.deletePostById(id);
        return "success";
    }

    @PutMapping("/updateblog/{id}")
    public String updatePost(@PathVariable("id") int id, @RequestBody Post updatedPost) {
        Post thepost = postService.getPostById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = authentication.getName();
        User user = userService.findByName(loggedInUser);
        if (authentication.getAuthorities().toString().equals("[ROLE_AUTHOR]") && !thepost.getAuthor().getName().equals(loggedInUser)) {
            return "access-denied";
        }

        thepost=updatedPost;
        postService.savePost(thepost);
        return "success";
    }


    @GetMapping("/")
    public List<Post> getAllPublishedPosts(Model model, @PageableDefault(size = 8) Pageable pageable) {
        Page<Post> postPage = postRepository.findByIsPublished(true, pageable);
        List<Post> posts = postPage.getContent();
        model.addAttribute("posts", posts);

        int totalPages = postPage.getTotalPages();
        if (totalPages == 0 && !posts.isEmpty()) {
            totalPages = 1;
        }

        model.addAttribute("currentPage", postPage.getNumber());
        model.addAttribute("totalPages", totalPages);

        return posts;
    }



}





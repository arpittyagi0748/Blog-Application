package com.io.mountblue.blogapplication.controller;

import com.io.mountblue.blogapplication.dao.PostRepository;
import com.io.mountblue.blogapplication.dao.TagRepository;
import com.io.mountblue.blogapplication.dao.UserRepository;
import com.io.mountblue.blogapplication.model.Post;
import com.io.mountblue.blogapplication.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

@Controller
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    @Autowired
    private final TagRepository tagRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    public PostController(PostService postService, PostRepository postRepository,TagRepository tagRepository,UserRepository userRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
        this.tagRepository=tagRepository;
        this.userRepository=userRepository;
    }

    @GetMapping("/newpost")
    public String showForm(Model theModel) {
        theModel.addAttribute("post", new Post());
        return "newpost";
    }

    @PostMapping("/processPost")
    public String createPost(Post post, Model model, String action, String tags) {
        postService.createPostWithTags(post, model, action, tags);
        postService.savePost(post);
        return "redirect:/newpost";
    }

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable("id") int id, Model model) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null) {
            model.addAttribute("post", post);
            model.addAttribute("comments", post.getComments());
            return "post";
        } else {
            return "error";
        }
    }

    @PostMapping("/deletePost/{id}")
    public String deletePost(@PathVariable("id") int id) {
        postService.deletePostById(id);
        return "redirect:/";
    }

    @GetMapping("/updateblog/{id}")
    public String updatePost(@PathVariable("id") int id, Model theModel, Post post) {
        Post thepost = postService.getPostById(id);
        postService.updatePost(theModel, thepost);
        return "newpost";
    }
    @GetMapping("/")
    public String getAllPublishedPosts(Model model, @PageableDefault(size = 8) Pageable pageable) {
       Page<Post> postPage = postRepository.findByIsPublished(true, pageable);
       List<Post> posts = postPage.getContent();
       model.addAttribute("posts", posts);

       int totalPages = postPage.getTotalPages();
       if (totalPages == 0 && !posts.isEmpty()) {
              totalPages = 1;
          }

       model.addAttribute("currentPage", postPage.getNumber());
       model.addAttribute("totalPages", totalPages);

       return "home";
    }


    @GetMapping("/sort")
    public String sortAndSearchPosts(Model model,
                                     @RequestParam(value = "search", required = false) String searchQuery,
                                     @RequestParam(value = "sort", required = false) String sortOption,
                                     HttpSession session,
                                     @PageableDefault(size = 8) Pageable pageable) {

          List<Post> storedPosts = (List<Post>) session.getAttribute("storedPosts");

        if (storedPosts == null || searchQuery != null) {
              storedPosts = postRepository.findByIsPublishedAndSearchQuery(true, searchQuery);
        } else if (storedPosts.isEmpty() && searchQuery == null) {
              storedPosts = postRepository.findByIsPublished(true);
        }

         List<Post> posts;

         if (sortOption != null && !sortOption.isEmpty()) {
             posts = new ArrayList<>(storedPosts);
            if ("published_at".equals(sortOption)) {
                posts.sort(Comparator.comparing(Post::getPublishedAt));
            } else if ("alphabetical".equals(sortOption)) {
                posts.sort(Comparator.comparing(Post::getTitle));
            }
         } else {
             posts = storedPosts;
         }

         session.setAttribute("storedPosts", posts);


         int pageSize = pageable.getPageSize();
         int currentPage = pageable.getPageNumber();
         int startItem = currentPage * pageSize;
         List<Post> paginatedPosts;

         if (posts.size() < startItem) {
             paginatedPosts = Collections.emptyList();
         } else {
            int toIndex = Math.min(startItem + pageSize, posts.size());
             paginatedPosts = posts.subList(startItem, toIndex);
         }

         model.addAttribute("posts", paginatedPosts);
         model.addAttribute("totalPages", Math.ceil((double) posts.size() / pageSize));
         model.addAttribute("currentPage", currentPage);

         return "home";
    }



    @GetMapping("/filterByAuthorAndTag")
    public String filterByAuthorAndTag(Model model,
                                        @RequestParam(value = "authors", required = false) List<String> authors,
                                        @RequestParam(value = "tags", required = false) List<String> tags,
                                        @RequestParam(value = "startDate", required = false) LocalDate startDate,
                                        @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                          HttpSession session,
                                        @PageableDefault(size = 8) Pageable pageable) {

         List<String> allAuthors = postRepository.findAllAuthors();
         List<String> allTags = tagRepository.findAllTags();

         model.addAttribute("authors", allAuthors);
         model.addAttribute("tags", allTags);

         List<Post> storedPosts = (List<Post>) session.getAttribute("storedPosts");

         if (storedPosts == null) {
             storedPosts = postRepository.findByIsPublished(true);
         }

         List<Post> filteredPosts = postService.filterPostsByAuthorsTagsAndDate(storedPosts, authors, tags, startDate, endDate);


         int pageSize = pageable.getPageSize();
         int currentPage = pageable.getPageNumber();
         int startItem = currentPage * pageSize;
         List<Post> paginatedPosts;

         if (filteredPosts.size() < startItem) {
             paginatedPosts = Collections.emptyList();
         } else {
             int toIndex = Math.min(startItem + pageSize, filteredPosts.size());
             paginatedPosts = filteredPosts.subList(startItem, toIndex);
         }

        model.addAttribute("posts", paginatedPosts);
        model.addAttribute("totalPages", Math.ceil((double) filteredPosts.size() / pageSize));
        model.addAttribute("currentPage", currentPage);

        return "home";
    }


}





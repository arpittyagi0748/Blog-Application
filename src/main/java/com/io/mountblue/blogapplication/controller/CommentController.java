package com.io.mountblue.blogapplication.controller;

import com.io.mountblue.blogapplication.model.Comment;
import com.io.mountblue.blogapplication.model.User;
import com.io.mountblue.blogapplication.service.CommentService;
import com.io.mountblue.blogapplication.service.PostService;
import com.io.mountblue.blogapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CommentController {

    private CommentService commentService;
    private PostService postService;
    private UserService userService;

    @Autowired
    public CommentController(CommentService commentService, PostService postService, UserService userService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping("/postComment")
    public String addComment(@ModelAttribute("newComment") Comment newComment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = authentication.getName();
        User user = userService.findByName(loggedInUser);
        newComment.setEmail(user.getEmail());
        newComment.setName(loggedInUser);
        commentService.addComment(newComment);

        return "redirect:/post/" + newComment.getPostId().getId();
    }

    @GetMapping("/editComment/{commentId}")
    public String showUpdateCommentPage(@PathVariable("commentId") int commentId, Model model) {
        Comment comment = commentService.getCommentById(commentId);
        model.addAttribute("comment", comment);
        return "update-comment";
    }

    @PostMapping("/updateComment/{commentId}")
    public String updateComment(@PathVariable("commentId") int commentId, @ModelAttribute("comment") String updatedComment,@ModelAttribute("postId") int id) {

        String updated = updatedComment;
        System.out.println(updated);
        commentService.updateComment(commentId, updated);
        return "redirect:/post/" + id;
    }

    @PostMapping("/deleteComment/{commentId}")
    public String deleteComment(@PathVariable("commentId") int commentId) {
        Comment comment = commentService.getCommentById(commentId);
        int postId = comment.getPostId().getId();
        commentService.deleteComment(commentId);
        return "redirect:/post/" + postId;
    }
}

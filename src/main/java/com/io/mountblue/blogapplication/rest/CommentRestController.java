package com.io.mountblue.blogapplication.rest;

import com.io.mountblue.blogapplication.model.Comment;
import com.io.mountblue.blogapplication.model.Post;
import com.io.mountblue.blogapplication.model.User;
import com.io.mountblue.blogapplication.service.CommentService;
import com.io.mountblue.blogapplication.service.PostService;
import com.io.mountblue.blogapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/myblog")
public class CommentRestController {
    private CommentService commentService;
    private PostService postService;
    private UserService userService;
    @Autowired
    public CommentRestController(CommentService commentService, PostService postService, UserService userService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping("/postComment/{postId}")
    public String addComment(@PathVariable("postId") int id, @RequestBody String commentstr,Comment comment) {
        Post post=postService.getPostById(id);
        comment.setPostId(post);
        comment.setComment(commentstr);
        return "success";
    }
    @GetMapping("/getComment/{postId}")
    public List getComment(@PathVariable("postId") int id) {
        Post post=postService.getPostById(id);
        List <Comment> comments=post.getComments();

        return comments;
    }



    @PutMapping("/updateComment/{commentId}")
    public String updateComment(@PathVariable("commentId") int commentId, @RequestBody String updatedComment) {
        Comment comment=commentService.getCommentById(commentId);
        Post thePost=comment.getPostId();
        String updated = updatedComment;
        System.out.println(updated);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = authentication.getName();
        User user = userService.findByName(loggedInUser);
        if (authentication.getAuthorities().toString().equals("[ROLE_AUTHOR]") && !thePost.getAuthor().getName().equals(loggedInUser)) {
            return "access-denied";
        }
        commentService.updateComment(commentId, updated);
        return "success";
    }

    @DeleteMapping("/deleteComment/{commentId}")
    public String deleteComment(@PathVariable("commentId") int commentId) {
        Comment comment = commentService.getCommentById(commentId);
        int postId = comment.getPostId().getId();
        Post thePost=comment.getPostId();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = authentication.getName();
        User user = userService.findByName(loggedInUser);
        if (authentication.getAuthorities().toString().equals("[ROLE_AUTHOR]") && !thePost.getAuthor().getName().equals(loggedInUser)) {
            return "access-denied";
        }
        commentService.deleteComment(commentId);
        return "success";
    }
}

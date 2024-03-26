//package com.io.mountblue.blogapplication.service;
//
//import com.io.mountblue.blogapplication.dao.CommentRepository;
//import com.io.mountblue.blogapplication.model.Comment;
//import com.io.mountblue.blogapplication.model.Post;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//public class CommentService {
//
//    @Autowired
//    private CommentRepository commentRepository;
//    @Autowired
//    private PostService postService;
//
//    //    public List<Comment> getAllCommentsByPostId(int postId) {
////        return commentRepository.findAllByPostId(postId);
////    }
//    public Comment getCommentById(int id){
//        return  commentRepository.findById(id).get();
//    }
//
//    public void addComment(Comment newComment) {
//        LocalDateTime now = LocalDateTime.now();
//        newComment.setName("Me");
//        newComment.setEmail("Me@Me");
//        newComment.setCreatedAt(now);
//        newComment.setUpdatedAt(now);
////        Post post = postService.getPostById(postId);
//        commentRepository.save(newComment);
//    }
//
//    public void updateComment(Comment updatedComment) {
//        LocalDateTime now = LocalDateTime.now();
//        updatedComment.setName("Me");
//        updatedComment.setEmail("Me@Me");
//        updatedComment.setCreatedAt(now);
//        updatedComment.setUpdatedAt(now);
//        updatedComment.setComment("This is updated");
////        updatedComment.setId(59);
////        updatedComment.setPostId();
//        commentRepository.save(updatedComment);
//    }
//
//
//    public void deleteComment(int commentId) {
//        commentRepository.deleteById(commentId);
//    }
//}
package com.io.mountblue.blogapplication.service;

import com.io.mountblue.blogapplication.dao.CommentRepository;
import com.io.mountblue.blogapplication.model.Comment;
import com.io.mountblue.blogapplication.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService implements CommentServiceInterface {

    private final CommentRepository commentRepository;
    private final PostService postService;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostService postService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
    }

    public Comment getCommentById(int id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.orElse(null);
    }

    public void addComment(Comment newComment) {
        LocalDateTime now = LocalDateTime.now();
        newComment.setCreatedAt(now);
        newComment.setUpdatedAt(now);
        commentRepository.save(newComment);
    }

    public void updateComment(int commentId, String update) {
        Comment existingComment = getCommentById(commentId);
        if (existingComment != null) {
            existingComment.setUpdatedAt(LocalDateTime.now());
            existingComment.setComment(update);
            commentRepository.save(existingComment);
        }
    }

    public void deleteComment(int commentId) {
        commentRepository.deleteById(commentId);
    }
}

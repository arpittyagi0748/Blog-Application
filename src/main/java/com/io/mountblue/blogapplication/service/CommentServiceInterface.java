package com.io.mountblue.blogapplication.service;

import com.io.mountblue.blogapplication.model.Comment;

import java.util.List;

public interface CommentServiceInterface {

    Comment getCommentById(int id);

    void addComment(Comment newComment);

    void updateComment(int commentId, String update);

    void deleteComment(int commentId);
}

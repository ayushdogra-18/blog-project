package com.ayush.blogproject.service;

import com.ayush.blogproject.model.Comments;

public interface CommentService {
    public void addCommnent(Long id, String name, String email, String comment);

    public Long getPostIdByCommentId(Long id);

    //    public void editCommnent(Long id);
    public Comments getCommentById(Long id);

    void updateComment(Long commentId, String name, String email, String comment);

    void deleteBlog(Long id, Long postId);
}

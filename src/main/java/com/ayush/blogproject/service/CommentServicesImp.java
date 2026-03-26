package com.ayush.blogproject.service;

import com.ayush.blogproject.model.Comments;
import com.ayush.blogproject.model.Posts;
import com.ayush.blogproject.repository.CommentsRepository;
import com.ayush.blogproject.repository.PostRepository;
import com.ayush.blogproject.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServicesImp implements CommentService {

    @Autowired
    CommentsRepository commentsRepository;
    PostRepository postRepository;

    public CommentServicesImp(CommentsRepository commentsRepository, PostRepository postRepository) {
        this.postRepository = postRepository;
        this.commentsRepository = commentsRepository;
    }

    @Transactional
    public void addCommnent(Long id, String name, String email, String comment) {

        Posts post = postRepository.findById(id).orElse(null);

        if (post == null) {
            throw new RuntimeException("Post not found with id: " + id);
        }
        Comments comments = new Comments();
        comments.setName(name);
        comments.setEmail(email);
        comments.setComment(comment);
        comments.setPost(post);
        commentsRepository.save(comments);
    }

    public Comments getCommentById(Long id) {
        return commentsRepository.findById(id).orElseThrow();
    }

    public Long getPostIdByCommentId(Long commentId) {
        Comments comment = commentsRepository.findById(commentId).orElseThrow();
        return comment.getPost().getId();
    }

    @Transactional
    public void deleteBlog(Long commentId, Long postId) {
        commentsRepository.deleteByIdAndPostId(commentId, postId);
    }

    @Override
    @Transactional
    public void updateComment(Long commentId, String name, String email, String comment) {
        Comments existing = commentsRepository.findById(commentId).orElseThrow();
        existing.setName(name);
        existing.setEmail(email);
        existing.setComment(comment);
        commentsRepository.save(existing);
    }
    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        commentsRepository.deleteById(commentId);
    }

}

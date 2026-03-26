package com.ayush.blogproject.restcontroller;

import com.ayush.blogproject.model.Comments;
import com.ayush.blogproject.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentsRestController {

    private final CommentService commentService;

    @Autowired
    public CommentsRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    // Comment add karo
    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody Comments comment) {
        if (comment.getPost() == null || comment.getPost().getId() == null) {
            return ResponseEntity.badRequest().body("Post ID is required");
        }
        commentService.addCommnent(
                comment.getPost().getId(),
                comment.getName(),
                comment.getEmail(),
                comment.getComment()
        );
        return ResponseEntity.ok("Comment added successfully");
    }

    // Comment update karo
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id,
                                           @RequestBody Comments comment) {
        commentService.updateComment(
                id,
                comment.getName(),
                comment.getEmail(),
                comment.getComment()
        );
        return ResponseEntity.ok("Comment updated successfully");
    }

    // Comment delete karo
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok("Comment deleted successfully");
    }
}
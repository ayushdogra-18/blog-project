package com.ayush.blogproject.repository;

import com.ayush.blogproject.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepository extends JpaRepository<Comments,Long> {

    void deleteByIdAndPostId(Long commentId, Long postId);
}

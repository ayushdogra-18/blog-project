package com.ayush.blogproject.repository;

import com.ayush.blogproject.model.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface PostRepository extends JpaRepository<Posts, Long> {

    @Query("SELECT p FROM Posts p WHERE p.isPublished = true")
    Page<Posts> findAllPublished(Pageable pageable);

    @Query("SELECT DISTINCT p.user.username FROM Posts p WHERE p.isPublished = true ORDER BY p.user.username")
    List<String> findDistinctAuthors();

    @Query(value = """
    SELECT * FROM (
        SELECT DISTINCT p.*, u.username as sort_username FROM posts p
        LEFT JOIN post_tags pt ON p.id = pt.post_id
        LEFT JOIN tags t ON pt.tags_id = t.id
        LEFT JOIN users u ON p.user_id = u.id
        WHERE p.is_published = true
        AND (CAST(:search AS TEXT) IS NULL
             OR LOWER(p.title) LIKE LOWER(CONCAT('%', CAST(:search AS TEXT), '%'))
             OR p.content ILIKE CONCAT('%', CAST(:search AS TEXT), '%')
             OR LOWER(u.username) LIKE LOWER(CONCAT('%', CAST(:search AS TEXT), '%')))
        AND (CAST(:author AS TEXT) IS NULL OR LOWER(u.username) = LOWER(CAST(:author AS TEXT)))
        AND (CAST(:tagId AS BIGINT) IS NULL OR t.id = CAST(:tagId AS BIGINT))
    ) sub
    """,
            countQuery = """
    SELECT COUNT(DISTINCT p.id) FROM posts p
    LEFT JOIN post_tags pt ON p.id = pt.post_id
    LEFT JOIN tags t ON pt.tags_id = t.id
    LEFT JOIN users u ON p.user_id = u.id
    WHERE p.is_published = true
    AND (CAST(:search AS TEXT) IS NULL
         OR LOWER(p.title) LIKE LOWER(CONCAT('%', CAST(:search AS TEXT), '%'))
         OR p.content ILIKE CONCAT('%', CAST(:search AS TEXT), '%')
         OR LOWER(u.username) LIKE LOWER(CONCAT('%', CAST(:search AS TEXT), '%')))
    AND (CAST(:author AS TEXT) IS NULL OR LOWER(u.username) = LOWER(CAST(:author AS TEXT)))
    AND (CAST(:tagId AS BIGINT) IS NULL OR t.id = CAST(:tagId AS BIGINT))
    """,
            nativeQuery = true)

    Page<Posts> findWithFilters(@Param("search") String search,
                                @Param("author") String author,
                                @Param("tagId") Long tagId,
                                Pageable pageable);

}
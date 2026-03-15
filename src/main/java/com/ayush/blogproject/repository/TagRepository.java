package com.ayush.blogproject.repository;

import com.ayush.blogproject.model.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tags,Integer> {
    Tags findByName(String tagName);
}

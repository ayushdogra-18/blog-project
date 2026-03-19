package com.ayush.blogproject.repository;

import com.ayush.blogproject.model.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//public interface TagRepository extends JpaRepository<Tags,Long> {
//    Tags findByName(String tagName);
//}

public interface TagRepository extends JpaRepository<Tags, Long> {

    //Tags findByName(String name);
    Tags findFirstByName(String name);

    List<Tags> findAllByOrderByNameAsc();
}

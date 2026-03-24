package com.ayush.blogproject.service;

import com.ayush.blogproject.dto.PostBlogDto;
import com.ayush.blogproject.dto.PostListDao;
import com.ayush.blogproject.model.Posts;
import com.ayush.blogproject.model.Tags;
import com.ayush.blogproject.model.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BlogService {
//    public void savePost(Posts posts,String tagNames,String action);
void savePost(Posts posts, String tagNames, String action, String author);
//    public Page<Posts> getAllPosts(int page,int limit,String sortBy);
    Page<Posts> getAllPosts(int page, int limit, String sortBy,String search, String author, Long tagId);
    public Posts getPostById(Long id);
    public Posts updateBlog(Posts post,String tagNames);
    public void deleteBlog(Long id);
    List<String> getAllAuthors();
    List<Tags> getAllTags();




}

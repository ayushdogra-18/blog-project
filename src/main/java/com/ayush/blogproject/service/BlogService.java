package com.ayush.blogproject.service;

import com.ayush.blogproject.dto.PostBlogDto;
import com.ayush.blogproject.dto.PostListDao;
import com.ayush.blogproject.model.Posts;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BlogService {
    public void savePost(Posts posts,String tagNames,String action);
    public List<Posts> getAllPosts();
    public Posts getPostById(Long id);
    public Posts updateBlog(Posts post,String tagNames);
    public void deleteBlog(Long id);




}

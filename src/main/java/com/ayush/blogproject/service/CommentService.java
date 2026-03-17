package com.ayush.blogproject.service;

public interface CommentService {
    public void addCommnent(Long id, String name,String email, String comment);
    public void editCommnent(Long id);
}

package com.ayush.blogproject.dto;

import lombok.Data;

@Data
public class PostBlogDto {
    private String title;
    private String excerpt;
    private String content;
    private String author;

}

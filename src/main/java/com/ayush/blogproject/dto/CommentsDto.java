package com.ayush.blogproject.dto;

import com.ayush.blogproject.model.BaseModel;
import lombok.Data;

@Data
public class CommentsDto extends BaseModel {
    private String name;
    private String email;
    private String comment;

}

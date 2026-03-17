package com.ayush.blogproject.mapper;

import com.ayush.blogproject.dto.CommentsDto;
import com.ayush.blogproject.model.Comments;

    //Entity -> DTO
    public class CommentsMapper {
        public static CommentsDto mapToCommentDto(Comments comments){
            CommentsDto commentsDto=new CommentsDto();
            commentsDto.setId(comments.getId());
            commentsDto.setName(comments.getName());
            commentsDto.setEmail(comments.getEmail());
            commentsDto.setComment(comments.getComment());
            commentsDto.setUpdatedAt(comments.getUpdatedAt());
            commentsDto.setCreatedAt(comments.getCreatedAt());
            return commentsDto;
        }
        // DTO --> Entity

            public static Comments mapToComment(CommentsDto commentsDto){
                Comments comments=new Comments();
                comments.setId(commentsDto.getId());
                comments.setName(commentsDto.getName());
                comments.setEmail(commentsDto.getEmail());
                comments.setComment(commentsDto.getComment());
                comments.setUpdatedAt(commentsDto.getUpdatedAt());
                comments.setCreatedAt(commentsDto.getCreatedAt());
                return comments;
            }
    }

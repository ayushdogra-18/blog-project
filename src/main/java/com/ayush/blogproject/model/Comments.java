package com.ayush.blogproject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Comments extends BaseModel {
    private String name;
    private String email;
    private String password;

    @ManyToOne
    private Posts post;
}

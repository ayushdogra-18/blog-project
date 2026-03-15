package com.ayush.blogproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(exclude = "posts")
public class Tags extends BaseModel{
    private String name;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private Set<Posts> posts=new HashSet<>();
}
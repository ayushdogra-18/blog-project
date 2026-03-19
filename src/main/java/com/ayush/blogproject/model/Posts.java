package com.ayush.blogproject.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(exclude = "tags")
@ToString(exclude = "tags")
public class Posts extends BaseModel{

    private String title;
    private String excerpt;
    private String content;
    private String author;
    private LocalDateTime publishedAt;
    private boolean isPublished;

    @ManyToMany
    @JoinTable(
            name="post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tags_id")
    )
    private Set<Tags> tags = new HashSet<>();


    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comments> comments = new ArrayList<>();
}

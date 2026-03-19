package com.ayush.blogproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.core.metrics.StartupStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(exclude = "posts")
@ToString(exclude = "posts")
public class Tags extends BaseModel{
    private String name;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private Set<Posts> posts=new HashSet<>();
}


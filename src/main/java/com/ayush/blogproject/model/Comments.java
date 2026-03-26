package com.ayush.blogproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String comment;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "post_id")
    private Posts post;

//    @JsonProperty("post")          // allows deserialization (JSON input)
//    public void setPost(Posts post) {
//        this.post = post;
//    }

}

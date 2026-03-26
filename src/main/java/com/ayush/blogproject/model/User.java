package com.ayush.blogproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;
    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        ROLE_ADMIN,
        ROLE_AUTHOR
    }
}
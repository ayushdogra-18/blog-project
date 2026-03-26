package com.ayush.blogproject.restcontroller;

import com.ayush.blogproject.model.Posts;
import com.ayush.blogproject.model.User;
import com.ayush.blogproject.repository.UserRepository;
import com.ayush.blogproject.service.BlogService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
public class BlogRestController {

    BlogService blogService;
    UserRepository userRepository;

    BlogRestController(BlogService blogService,UserRepository userRepository){
        this.blogService=blogService;
        this.userRepository=userRepository;
    }

    @PostMapping("/api/blogs")
    public ResponseEntity<?> createBlog(
            @RequestBody Posts posts,
            @RequestParam String tagNames,
            @RequestParam String action,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        blogService.savePost(posts, tagNames, action, username);

        return ResponseEntity.ok("Blog created successfully");
    }

    @GetMapping("/api/blog")
    public ResponseEntity<?>  blog(@RequestParam(defaultValue = "0") int start,
                            @RequestParam(defaultValue = "9") int limit,
                            @RequestParam(defaultValue = "title") String sortBy,
                            @RequestParam(defaultValue = "") String search,
                            @RequestParam(defaultValue = "") String author,
                            @RequestParam(required = false) Long tagId,
                            @AuthenticationPrincipal UserDetails userDetails,
                            Model model) {

        int page = start / limit;

        Page<Posts> posts = blogService.getAllPosts(page, limit, sortBy, search, author, tagId);
        return ResponseEntity.ok(posts);
    }

    @PutMapping("/api/blogs/{id}")
    public ResponseEntity<?> updatePublishBlog(@RequestBody Posts post,
                                               @PathVariable Long id){
        post.setId(id);
        Posts posts = blogService.updateBlog(post,post.getTags().toString());
        return ResponseEntity.ok("updated");
    }

    @DeleteMapping("/api/blogs/{id}")
    public ResponseEntity<?> deleteBlog(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        blogService.deleteBlog(id);
        return ResponseEntity.ok("Deleted successfully");
    }

}

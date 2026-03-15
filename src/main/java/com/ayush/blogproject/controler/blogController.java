package com.ayush.blogproject.controler;

import com.ayush.blogproject.dto.PostBlogDto;
import com.ayush.blogproject.model.Posts;
import com.ayush.blogproject.service.BlogService;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.annotations.Collate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class blogController {

      BlogService blogService;

   @Autowired
   blogController(BlogService blogService){
       this.blogService=blogService;
   }


    @GetMapping("/")
    public  String home() {
        return "home";
    }


    @GetMapping("/createposts")
    public  String createBlogController(Model model) {
        Posts posts=new Posts();
        model.addAttribute("blog",posts);
       return "createposts";
    }

    @PostMapping("/blogs")
    public  String postBlogController(@ModelAttribute("blog") Posts posts,
                                      @RequestParam String tagNames,
                                      @RequestParam String action) {

        System.out.println(action);
        blogService.savePost(posts,tagNames,action);
        return "redirect:/blog";
    }

    @GetMapping("/blog")
    public String blog(Model model){
        List<Posts> posts = blogService.getAllPosts();
        model.addAttribute("posts", posts);
        return "blog";
    }

    @GetMapping("/sortblog")
    public String sortBlog(HttpServletRequest req, Model model){
        String sorted = req.getParameter("sort");
        List<Posts> posts = blogService.getAllPosts();
        if("sorted".equals(sorted)) {
            posts.sort(Comparator.comparing(Posts::getPublishedAt));
        }
        model.addAttribute("posts", posts);
        model.addAttribute("currentSort", sorted);
        return "blog";
    }


    @GetMapping("/readmore")
    public String readMore(HttpServletRequest req, Model model){
       String id=req.getParameter("id");
        Integer postId = Integer.parseInt(id);
       Posts post=blogService.readMore(postId);
       model.addAttribute("post",post);
        return "readmore";
    }

    @PostMapping("/addcomment")
    public String addComment(HttpServletRequest req, Model model){
        String id=req.getParameter("id");
        Integer postId = Integer.parseInt(id);
        Posts post=blogService.readMore(postId);
        model.addAttribute("post",post);
        return "readmore";
    }
}

package com.ayush.blogproject.controler;

import com.ayush.blogproject.model.Posts;
import com.ayush.blogproject.model.Tags;
import com.ayush.blogproject.service.BlogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class BlogController {

      BlogService blogService;

   @Autowired
   BlogController(BlogService blogService){
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


    @GetMapping("/updateblog")
    public String readMore(HttpServletRequest req, Model model){
       String id=req.getParameter("id");
        Long postId = Long.parseLong(id);
       Posts post=blogService.getPostById(postId);
       model.addAttribute("post",post);
        return "updateblog";
    }


    @PostMapping("/updatepost")
    public String updatePost(@RequestParam("postId") Long id, Model model){
        Posts post=blogService.getPostById(id);
        List<String> list = post.getTags().stream().map( tags -> tags.getName()).toList();
        String tags=String.join(",",list);
        model.addAttribute("blog",post);
        model.addAttribute("tags",tags);

        return "createposts";
    }

    //update edit blog
    @PostMapping("/updatepublishblog")
    public String updatePublishBlog(@ModelAttribute("blog") Posts post, Model model,
                                    @ModelAttribute("blog") Posts posts,
                                    @RequestParam String tagNames,RedirectAttributes redirectAttributes){
        Posts updatedPost=blogService.updateBlog(post,tagNames);
        model.addAttribute("blog",updatedPost);
        blogService.updateBlog(posts,tagNames);
        redirectAttributes.addAttribute("id", post.getId());
        return "redirect:/updateblog";
    }

    @PostMapping("/deletepost")
    public String deletePost(@RequestParam("postId") Long id, Model model){
       blogService.deleteBlog(id);
        return "redirect:/blog";
    }


}

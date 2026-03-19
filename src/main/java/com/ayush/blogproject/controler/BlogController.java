package com.ayush.blogproject.controler;

import com.ayush.blogproject.model.Comments;
import com.ayush.blogproject.model.Posts;
import com.ayush.blogproject.service.BlogService;
import com.ayush.blogproject.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class BlogController {

    BlogService blogService;
    CommentService commentService;

    @Autowired
    BlogController(BlogService blogService, CommentService commentService) {
        this.blogService = blogService;
        this.commentService = commentService;
    }


    @GetMapping("/")
    public String home() {
        return "home";
    }


    @GetMapping("/createposts")
    public String createBlogController(Model model) {
        Posts posts = new Posts();
        model.addAttribute("blog", posts);
        return "createposts";
    }

    @PostMapping("/blogs")
    public String postBlogController(@ModelAttribute("blog") Posts posts,
                                     @RequestParam String tagNames,
                                     @RequestParam String action) {
        System.out.println(action);
        System.out.println(posts.getAuthor() + "   authorrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
        blogService.savePost(posts, tagNames, action);
        return "redirect:/blog";
    }

    @GetMapping("/blog")
    public String blog(@RequestParam(defaultValue = "0") int start,
                       @RequestParam(defaultValue = "9") int limit,
                       @RequestParam(defaultValue = "title") String sortBy,
                       @RequestParam(defaultValue = "") String search,
                       @RequestParam(defaultValue = "") String author,
                       @RequestParam(required = false) Long tagId,
                       Model model) {

        int page = start / limit;

        Page<Posts> posts = blogService.getAllPosts(page, limit, sortBy, search, author, tagId);

        model.addAttribute("posts", posts.getContent());
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("start", start);
        model.addAttribute("limit", limit);
        model.addAttribute("search", search);
        model.addAttribute("author", author);
        model.addAttribute("tagId", tagId);
        model.addAttribute("allTags", blogService.getAllTags());
        model.addAttribute("allAuthors", blogService.getAllAuthors());

        return "blog";
    }

    @GetMapping("/updateblog")
    public String readMore(HttpServletRequest req, Model model) {

        String id = req.getParameter("id");
        Long postId = Long.parseLong(id);

        Posts post = blogService.getPostById(postId);
        model.addAttribute("post", post);

        String commentIdStr = req.getParameter("commentId");
        if (commentIdStr != null) {
            Long commentId = Long.parseLong(commentIdStr);
            Comments comment = commentService.getCommentById(commentId);
            model.addAttribute("editComment", comment);
        }
        return "updateblog";
    }


    @PostMapping("/updatepost")
    public String updatePost(@RequestParam("postId") Long id, Model model) {
        Posts post = blogService.getPostById(id);
        List<String> list = post.getTags().stream().map(tags -> tags.getName()).toList();
        String tags = String.join(",", list);
        model.addAttribute("blog", post);
        model.addAttribute("tags", tags);

        return "createposts";
    }

    //update edit blog
    @PostMapping("/updatepublishblog")
    public String updatePublishBlog(@ModelAttribute("blog") Posts post, Model model,
                                    @ModelAttribute("blog") Posts posts,
                                    @RequestParam String tagNames, RedirectAttributes redirectAttributes) {
        blogService.updateBlog(post, tagNames);
        redirectAttributes.addAttribute("id", post.getId());
        return "redirect:/updateblog";
    }

    @PostMapping("/deletepost")
    public String deletePost(@RequestParam("postId") Long id,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        blogService.deleteBlog(id);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/blog";
    }

}

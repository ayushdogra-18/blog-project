package com.ayush.blogproject.controler;

import com.ayush.blogproject.service.BlogService;
import com.ayush.blogproject.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CommentsController {
   CommentService commentService;
    @Autowired
    CommentsController(CommentService commentService){
        this.commentService=commentService;
    }
    @PostMapping("/addcomment")
    public String addComments(@RequestParam("postId") Long id,
                              @RequestParam String name,
                              @RequestParam String email,
                              @RequestParam String comment, RedirectAttributes redirectAttributes){
        System.out.println("POST ID RECEIVED: " + id);

        commentService.addCommnent(id,name,email,comment);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/updateblog";
    }

    @PostMapping("/editcomments")
    public String EditComments(@RequestParam("postId") Long id,
                               Model model){

        commentService.editCommnent(id);
        return "redirect:/updateblog";
    }
}

package com.ayush.blogproject.controler;

import com.ayush.blogproject.service.BlogService;
import com.ayush.blogproject.service.CommentService;
import org.jspecify.annotations.NonNull;
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
    CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/addcomment")
    public String addComments(@RequestParam("postId") Long id,
                              @RequestParam String name,
                              @RequestParam String email,
                              @RequestParam String comment,
                              @RequestParam(required = false) Long commentId,
                              RedirectAttributes redirectAttributes) {
        if (commentId != null && commentId != 0) {
            commentService.updateComment(commentId, name, email, comment);
        } else {
            commentService.addCommnent(id, name, email, comment);
        }
        redirectAttributes.addAttribute("id", id);
        return "redirect:/updateblog";
    }

    @PostMapping("/editcomments")
    public String EditComments(@RequestParam("commentId") Long id,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        Long postId = commentService.getPostIdByCommentId(id);
        redirectAttributes.addAttribute("id", postId);
        redirectAttributes.addAttribute("commentId", id);
        return "redirect:/updateblog";
    }

    @PostMapping("/deletecomment")
    public String deleteComment(@RequestParam("commentId") Long cid,
                                @RequestParam("postId") Long pid,
                                Model model, RedirectAttributes redirectAttributes) {
        commentService.deleteBlog(cid, pid);
        redirectAttributes.addAttribute("id", pid);
        return "redirect:/updateblog";
    }


}

package be.intec.themarujohyperblog.controller;

import be.intec.themarujohyperblog.model.BlogComment;
import be.intec.themarujohyperblog.model.BlogPost;
import be.intec.themarujohyperblog.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CommentController {


    private final CommentService commentService;
    private PostServiceImpl postService;

    public CommentController(CommentService commentService, @Lazy PostServiceImpl postService) {
        this.commentService = commentService;
        this.postService = postService;
    }


    //Handles GET request for displaying all comments for a specific post
   //JDR: this will be handled in the post controller
    /*
    @GetMapping("/blog_post/{blogpostId}/blog_comment")
    public String getAllCommentsByPostId(@PathVariable(value = "blogpostId") Long postId,
                                         @RequestParam(name = "page", defaultValue = "1") int pageNo,
                                         Model model){
        int pageSize = 5;
        Page<BlogComment> page = commentService.findCommentPaginated(postId, pageNo, pageSize);
        List<BlogComment> commentList = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("commentList", commentList);

        // Fetches the post to which comments belong and adds to the model
        BlogPost post = postService.getPostById(postId);
        model.addAttribute("blog_post", post);
        model.addAttribute("comment", new BlogComment());
        return "blogpostdetail";
    }
    */
    //end

    // Handles POST requests for creating a new comment
    //JDR: this will be handled in the post controller
    /*
    @PostMapping("/blog_post/{blogpostId}/blog_comment")
    public String createComment(@PathVariable(value = "blogpostId") Long postId,
                                @ModelAttribute("comment") BlogComment comment) {
        BlogPost post = postService.getPostById(postId);
        // Associates the comment with the post
        comment.setPost(post);
        commentService.saveComment(comment);
        return "redirect:/blog_post/" + postId + "/blog_comment";
    }

     */

    // Handles POST requests for updating an existing comment
    @PostMapping("/blog_post/{blogpostId}/blog_comment/{blogcommentId}/edit")
    public String updateComment(@PathVariable(value = "blogpostId") Long postId,
                                @PathVariable(value = "blogcommentId") Long commentId,
                                @ModelAttribute("comment") BlogComment comment) {
        BlogComment existingComment = commentService.findCommentById(commentId);
        existingComment.setText(comment.getText());
        commentService.saveComment(existingComment);
        return "redirect:/blog_post/" + postId + "/blog_comment";
    }

    // Handles GET requests for deleting a specific comment
    //JDR: this will be handled in the post controller
    /*
    @GetMapping("/blog_post/{blogpostId}/blog_comment/{blogcommentId}/delete")
    public String deleteComment(@PathVariable(value = "blogpostId") Long postId,
                                @PathVariable(value = "blogcommentId") Long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/blog_post/" + postId + "/blog_comment";
    }
    */

}

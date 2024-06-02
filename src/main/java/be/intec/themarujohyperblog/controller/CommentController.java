package be.intec.themarujohyperblog.controller;

import be.intec.themarujohyperblog.model.BlogComment;
import be.intec.themarujohyperblog.model.BlogPost;
import be.intec.themarujohyperblog.service.CommentServiceImpl;
import be.intec.themarujohyperblog.service.PostServiceImpl;
import be.intec.themarujohyperblog.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CommentController {

    private final CommentServiceImpl commentService;
    private final PostServiceImpl postService;
    private final UserServiceImpl userService;

    @Autowired
    public CommentController(CommentServiceImpl commentService, PostServiceImpl postService, UserServiceImpl userService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    // Handles GET request for displaying a specific post with its comments
    @GetMapping("/posts/{postId}")
    public String getPostDetails(@PathVariable(value = "postId") Long postId,
                                 @RequestParam(name = "page", defaultValue = "1") int pageNo,
                                 Model model) {
        int pageSize = 5;
        Page<BlogComment> page = commentService.findCommentPaginated(postId, pageNo, pageSize);
        List<BlogComment> commentList = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("comments", commentList);

        // Fetches the post to which comments belong and adds to the model
        BlogPost post = postService.getPostById(postId);
        model.addAttribute("post", post);
        model.addAttribute("newComment", new BlogComment());
        return "blogpostdetail";
    }

    // Handles POST requests for creating a new comment
    @PostMapping("/posts/{postId}/comments")
    public String createComment(@PathVariable(value = "postId") Long postId,
                                @ModelAttribute("newComment") BlogComment comment) {
        BlogPost post = postService.getPostById(postId);
        // Associates the comment with the post
        comment.setPost(post);
        commentService.saveComment(comment);
        return "redirect:/posts/" + postId;
    }

    // Handles POST requests for updating an existing comment
    @PostMapping("/posts/{postId}/comments/{commentId}/edit")
    public String updateComment(@PathVariable(value = "postId") Long postId,
                                @PathVariable(value = "commentId") Long commentId,
                                @ModelAttribute("comment") BlogComment comment) {
        BlogComment existingComment = commentService.findCommentById(commentId);
        existingComment.setText(comment.getText());
        commentService.saveComment(existingComment);
        return "redirect:/posts/" + postId;
    }

    // Handles GET requests for deleting a specific comment
    @GetMapping("/posts/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable(value = "postId") Long postId,
                                @PathVariable(value = "commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/posts/" + postId;
    }

}

package be.intec.themarujohyperblog.controller;

import be.intec.themarujohyperblog.model.BlogComment;
import be.intec.themarujohyperblog.model.BlogPost;
import be.intec.themarujohyperblog.service.*;
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
        return "blog_comment";
    }

    // Handles POST requests for creating a new comment
    @PostMapping("/blog_post/{blogpostId}/blog_comment")
    public String createComment(@PathVariable(value = "blogpostId") Long postId,
                                @ModelAttribute("comment") BlogComment comment) {
        BlogPost post = postService.getPostById(postId);
        // Associates the comment with the post
        comment.setPost(post);
        commentService.saveComment(comment);
        return "redirect:/blog_post/" + postId + "/blog_comment";
    }

    // Handles POST requests for updating an existing comment
    @PostMapping("/blog_post/{blogpostId}/blog_comment/{blogcommentId}/edit")
    public String updateComment(@PathVariable(value = "blogpostId") Long postId,
                                @PathVariable(value = "blogcommentId") Long commentId,
                                @ModelAttribute("comment") BlogComment comment) {
        BlogComment existingComment = commentService.getCommentById(commentId);
        existingComment.setText(comment.getText());
        commentService.saveComment(existingComment);
        return "redirect:/blog_post/" + postId + "/blog_comment";
    }

    // Handles GET requests for deleting a specific comment
    @GetMapping("/blog_post/{blogpostId}/blog_comment/{blogcommentId}/delete")
    public String deleteComment(@PathVariable(value = "blogpostId") Long postId,
                                @PathVariable(value = "blogcommentId") Long commentId) {
        commentService.deleteCommentById(commentId);
        return "redirect:/blog_post/" + postId + "/blog_comment";
    }
   /* @PostMapping("/comments")
    public String saveComment(@RequestParam Long postId, @RequestParam String content) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUserName(username).orElseThrow(() -> new IllegalArgumentException("Invalid username: " + username));

        BlogPost post = postService.findById(postId);

        BlogComment comment = new BlogComment();
        comment.setCreatedAt(new Date());
        comment.setUser(user);
        comment.setPost(post);

        commentService.saveComment(comment);
        return "redirect:/posts/" + postId;
    }*/

    /*@GetMapping("/edit-comment/{id}")
    public String editComment(@PathVariable Long id, Model model) {
        BlogComment comment = commentService.findCommentById(id);
        model.addAttribute("comment", comment);
        return "edit_comment";
    }

    @PostMapping("/edit-comment/{id}")
    public String updateComment(@PathVariable Long id, @RequestParam String content) {
        BlogComment comment = commentService.findCommentById(id);
        commentService.saveComment(comment);
        return "redirect:/posts/" + comment.getPost().getId();
    }

    @GetMapping("/delete-comment/{id}")
    public String deleteComment(@PathVariable Long id) {
        BlogComment comment = commentService.findCommentById(id);
        commentService.deleteComment(comment);
        return "redirect:/posts/" + comment.getPost().getId();
    }*/
}

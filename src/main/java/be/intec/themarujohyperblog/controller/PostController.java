/*package be.intec.themarujohyperblog.controller;

import be.intec.themarujohyperblog.model.BlogPost;
import be.intec.themarujohyperblog.model.User;
import be.intec.themarujohyperblog.service.PostService;
import be.intec.themarujohyperblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
public class PostController {

    private final PostService postService;

    private  final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<BlogPost> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "blogcentral";
    }

    @GetMapping("/posts/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        BlogPost post = postService.findById(id);
        model.addAttribute("post", post);
        return "post";
    }

    @GetMapping("/create_post")
    public String createPost(Model model) {
        model.addAttribute("post", new BlogPost());
        return "create_post";
    }

    @PostMapping("/create_post")
    public String savePost(BlogPost post) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUserName(username).orElseThrow(() -> new IllegalArgumentException("Invalid username: " + username));

        post.setUser(user);
        post.setCreatedAt(new Date());
        postService.saveBlogPost(post);
        return "redirect:/";
    }

    @GetMapping("/edit_post/{id}")
    public String editPost(@PathVariable Long id, Model model) {
        BlogPost post = postService.findById(id);
        model.addAttribute("post", post);
        return "create_post";
    }

    @PostMapping("/edit-post/{id}")
    public String updatePost(@PathVariable Long id, BlogPost post) {
        BlogPost existingPost = postService.findById(id);
        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        postService.saveBlogPost(existingPost);
        return "redirect:/posts/" + id;
    }

    @GetMapping("/delete-post/{id}")
    public String deletePost(@PathVariable Long id) {
        BlogPost post = postService.findById(id);
        postService.delete(post);
        return "redirect:/";
    }

    @PostMapping("/like-post/{id}")
    public String likePost(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUserName(username).orElseThrow(() -> new IllegalArgumentException("Invalid username: " + username));

        BlogPost post = postService.findById(id);
        user.getLikedPosts().add(post);
        userService.saveUser(user);
        return "redirect:/posts/" + id;
    }
}*/

package be.intec.themarujohyperblog.controller;

import be.intec.themarujohyperblog.model.Like;
import be.intec.themarujohyperblog.model.BlogPost;
import be.intec.themarujohyperblog.model.User;
import be.intec.themarujohyperblog.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;



@Controller
@RequestMapping("/")
public class PostController {
    private final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final PostServiceImpl postService;
    private final UserServiceImpl userService;
    private final LikeServiceImpl likeService;

    @Autowired
    public PostController(PostServiceImpl postService, UserServiceImpl userService, LikeServiceImpl likeService) {
        this.postService = postService;
        this.userService = userService;
        this.likeService = likeService;
    }

    @GetMapping("/")  //root, eerste pagina
    public String viewHomePage(Model model) {
        return findPostPaginated(1,model); //dit beperkt ons tot 1 pagina?
    }

   /* @GetMapping("/{id}")
    public String getPostById(@PathVariable("id") Long id, Model model) {
        Optional<BlogPost> post = Optional.ofNullable(postService.findPostPaginated()ById(id));
        if (post.isPresent()) {
            model.addAttribute("post", post.get());
            return "post-details";
        } else {
            return "error";
        }
    }
*/
    @GetMapping("/showNewPostForm")
    public String showCreatePostForm(Model model) {
        BlogPost post = new BlogPost();
        model.addAttribute("post", post);
        return "createpost";
    }

    @PostMapping("/createPost")
    public String createPost(@ModelAttribute("post") BlogPost post, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            logger.warn("User session not found, redirecting to login.");
            return "redirect:/login";
        }
        List<BlogPost> userPosts = postService.getPostsByUser(user);
        logger.info("Creating post for user: {}", user.getUsername());
        post.setUser(user);
        postService.savePost(post);
        model.addAttribute("posts", userPosts);
        return "afterlogin";
    }

    @GetMapping("/updatePost/{id}")
    public String showUpdatePostForm(@PathVariable("id") Long postId, Model model) {
        BlogPost post = postService.getPostById(postId);
        model.addAttribute("post", post);
        return "editPost";
    }

    @PostMapping("/updatePost/{id}")
    public String updatePost(@PathVariable("id") Long id, @ModelAttribute("post") BlogPost post) {
        post.setId(id);
        postService.savePost(post);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/deletePost/{id}")
    public String deletePost(@PathVariable("id") Long postId) {
        postService.deletePost(postId);
        return "redirect:/posts";
    }

    @PostMapping("/like")
    @ResponseBody
    public Like likePost(@RequestBody Like like) {
        return likeService.saveLike(like);
    }

    @DeleteMapping("/unlike/{id}")
    @ResponseBody
    public void unlikePost(@PathVariable("id") Long id) {
        likeService.deleteLike(id);
    }


    @GetMapping("/page/{pageNo}")
    public String findPostPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 6; //aantal posts op één pagina is 6;
        Page<BlogPost> page = postService.findPostPaginated(pageNo, pageSize);
        List<BlogPost> postList = page.getContent(); //komt van springframework.data.domain.Page
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements()); //totaal aantal elementen op pagina
        model.addAttribute("postList", postList);
        return "blogcentral";
    }
}


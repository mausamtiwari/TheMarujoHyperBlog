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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;


@Controller
public class PostController {

    private final PostServiceImpl postService;
    private final UserServiceImpl userService;
    private final LikeServiceImpl likeService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public PostController(PostServiceImpl postService, UserServiceImpl userService, LikeServiceImpl likeService) {
        this.postService = postService;
        this.userService = userService;
        this.likeService = likeService;
    }

    @GetMapping("/")  //root, eerste pagina
    public String viewHomePage(Model model) {
        return findPostPaginated(1, model); //dit beperkt ons tot 1 pagina?
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
    public String showCreatePostForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        BlogPost post = new BlogPost();
        model.addAttribute("post", post);
        return "createpost";
    }

    @PostMapping("/createPost")
    public String createPost(@ModelAttribute("post") BlogPost post, @RequestParam("image") MultipartFile imageFile, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            logger.warn("User session not found, redirecting to login.");
            return "redirect:/login";
        }

        if (post.getDescription() == null || post.getDescription().trim().isEmpty()) {
            post.setDescription("No description available");
        }

        if (!imageFile.isEmpty()) {
            try {
                byte[] imageData = imageFile.getBytes();

                // Logging image size
                long imageSize = imageData.length;
                logger.info("Uploaded image size: {} bytes", imageSize);

                // Ensure image size does not exceed 10MB
                if (imageSize > 10 * 1024 * 1024) {
                    logger.error("File size exceeds 10MB limit.");
                    model.addAttribute("error", "File size exceeds 10MB limit.");
                    // You may also want to return to the form with the previously entered data
                    return "createPost";
                }

                post.setImageData(imageData);
            } catch (IOException e) {
                logger.error("Error processing image upload: {}", e.getMessage(), e);
                model.addAttribute("error", "Error processing image upload.");
                // Return to the form with an error message
                return "createPost";
            }
        }

        try {
            logger.info("Creating post for user: {}", user.getUsername());
            post.setCreatedAt(new Date());
            post.setUpdatedAt(new Date());
            post.setUser(user);
            postService.savePost(post);
        } catch (Exception e) {
            logger.error("Error saving post: {}", e.getMessage(), e);
            model.addAttribute("error", "Error saving post.");
            // Return to the form with an error message
            return "createPost";
        }

        // Redirect to the user's post list page after successful creation
        return "redirect:/createPost";
    }





   /* @GetMapping("/afterlogin")
    public String afterLogin(Model model, HttpSession session, @RequestParam(value = "page", defaultValue = "1") int page) {
        int pageSize = 6; // number of posts per page
        Page<BlogPost> postPage = postService.findPostPaginated(page, pageSize);
        List<BlogPost> posts = postPage.getContent();

        logger.info("Rendering afterlogin with currentPage: {}, totalPages: {}", page, postPage.getTotalPages());

        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("session", session);
        return "afterlogin";
    }*/

    @GetMapping("/afterlogin")
    public String afterLogin(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        List<BlogPost> getPosts = postService.getPostsByUser(user);
        model.addAttribute("posts", getPosts);
        //model.addAttribute("session", session);
        return "afterlogin";
    }


    @GetMapping("/myPosts")
    public String viewUserPosts(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        List<BlogPost> userPosts = postService.getPostsByUser(user);
        model.addAttribute("posts", userPosts);
        return "userposts";
    }

    /* @GetMapping("/myPosts/{pageNo}")
     public String viewUserPosts(@PathVariable(value = "pageNo") int pageNo, Model model, HttpSession session) {
         User user = (User) session.getAttribute("loggedInUser");
         if (user == null) {
             return "redirect:/login";
         }
         int pageSize = 6; // a number of posts per page
         Page<BlogPost> page = postService.findUserPostsPaginated(user, pageNo, pageSize);
         List<BlogPost> userPosts = page.getContent();
         model.addAttribute("currentPage", pageNo);
         model.addAttribute("totalPages", page.getTotalPages());
         model.addAttribute("totalItems", page.getTotalElements());
         model.addAttribute("posts", userPosts);
         return "userposts";
     }
 */
    /*  @GetMapping("/showNewPostForm")
   public String showNewPostForm(Model model) {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
           String username = authentication.getName();
           User user = userService.findByUserName(username).orElseThrow(() -> new IllegalArgumentException("Invalid username: " + username));
           model.addAttribute("user", user);
       } else {
           return "createpost";
       }

       BlogPost post = new BlogPost();
       model.addAttribute("post", post);
       return "createpost";
   }

    @PostMapping("/createPost")
    public String createPost(@ModelAttribute("post") BlogPost post) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getName().equals("anonymousUser")) {
            throw new IllegalArgumentException("Invalid username: anonymousUser");
        }

        String username = authentication.getName();
        User user = userService.findByUserName(username).orElseThrow(() -> new IllegalArgumentException("Invalid username: " + username));
        post.setUser(user);
        postService.savePost(post);
        return "afterlogin";
    }*/
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
        return "redirect:/myPosts/";
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


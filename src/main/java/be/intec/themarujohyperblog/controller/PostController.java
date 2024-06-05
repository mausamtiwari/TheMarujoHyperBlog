


package be.intec.themarujohyperblog.controller;

import be.intec.themarujohyperblog.model.BlogComment;
import be.intec.themarujohyperblog.model.Like;
import be.intec.themarujohyperblog.model.BlogPost;
import be.intec.themarujohyperblog.model.User;
import be.intec.themarujohyperblog.repository.UserRepository;
import be.intec.themarujohyperblog.service.*;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;



@Controller
public class PostController {

    private PostServiceImpl postService;
    private final UserServiceImpl userService;
    private final LikeServiceImpl likeService;

    private final UserRepository userRepository;

    private final CommentServiceImpl commentService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";
  
    @Autowired
    public PostController( UserServiceImpl userService, LikeServiceImpl likeService, UserRepository userRepository, CommentServiceImpl commentService) {
        this.userService = userService;
        this.likeService = likeService;
        this.userRepository = userRepository;
        this.commentService = commentService;
    }

    @Autowired
    public void setPostService(@Lazy PostServiceImpl postService) {
        this.postService = postService;

    }

    @GetMapping("/")  //root, eerste pagina
    public String viewHomePage(Model model) {
        return findPostPaginated(1, model); //Begint met pagina 1
    }

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
    public String createPost(@ModelAttribute("post") BlogPost post, @RequestParam("file") MultipartFile file, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            logger.warn("User session not found, redirecting to login.");
            return "redirect:/login";
        }


        // Handle file upload
        if (!file.isEmpty()) {
            String uploadError = handleFileUpload(file, post);
            if (uploadError != null) {
                model.addAttribute("error", uploadError);
                return "redirect:/createpost"; // Adjust the view name as necessary
            }
        }





        List<BlogPost> userPosts = postService.getPostsByUser(user);

        logger.info("Creating post for user: {}", user.getUsername());
        post.setCreatedAt(new Date());//JDR
        post.setUpdatedAt(new Date());//JDR
        post.setUser(user);
        postService.savePost(post);
      


      
        return "redirect:/afterlogin"; // Adjust the view name as necessary

    }

    private String handleFileUpload(MultipartFile file, BlogPost post) {
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.write(path, bytes);
            post.setPostPhoto(file.getOriginalFilename()); // Assuming the BlogPost entity has an 'image' field
            return null; // No error
        } catch (IOException e) {
            logger.error("Error uploading image", e);
            return "Failed to upload image. Please try again.";
        }
    }

    @GetMapping("/afterlogin")
    public String afterLogin(
            @RequestParam(name = "page", defaultValue = "1") int pageNo,
            Model model, HttpSession session) {

        int pageSize = 6; // number of posts per page
        Page<BlogPost> postPage = postService.findPostPaginated(pageNo, pageSize);
        List<BlogPost> posts = postPage.getContent();

        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("totalItems", postPage.getTotalElements());

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

  
   

    @GetMapping("/updatePost/{postId}")

    public String showUpdatePostForm(@PathVariable("postId") Long postId, Model model) {
    //Authorisation gebeurt in PostMapping, niet mogelijk hier httpsession te krijgen

        BlogPost post = postService.getPostById(postId);

        model.addAttribute("post", post);


        return "redirect:/saveUpdatedPost/{postId}";
    }


    @PostMapping("/saveUpdatedPost/{postId}")

    public String updatePost(@PathVariable("postId") Long postId, @ModelAttribute("post") BlogPost post, HttpSession session) {
        //Check voor niet-ingelogde visitor
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        BlogPost existingPost = postService.getPostById(postId);

        //check of de session id == post.user.id
        if (!Objects.equals(post.getUser().getId(), user.getId())) {
            //niet geauthoriseerd: post behoort niet tot de user
            return "redirect:/notAuthorised";
        }  else {

        existingPost.setTitle(post.getTitle());
        existingPost.setDescription(post.getDescription());
        existingPost.setContent(post.getContent());
        //existingPost.setComments(post.getComments());
        existingPost.setUpdatedAt(new Date());
        postService.savePost(existingPost);

        }

        return "redirect:/viewPost/{postId}";
    }

    //delete post and check the identity of the logged user
    @PostMapping("/deletePost/{id}")
    public String deletePost(@PathVariable("id") Long postId, HttpSession session) {
        //get session identity
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/notAuthorised";
        }
        ;
        BlogPost post = postService.getPostById(postId);
        //check if the post belongs to the user:
        if (!Objects.equals(post.getUser().getId(), user.getId())) {
            //niet geauthoriseerd: post behoort niet tot de user
            return "redirect:/notAuthorised";
        } else {
            postService.deletePost(postId);
        }
        return "redirect:/myPosts";


    }

    //delete blogpost comment for a specific postid and check the identity of the logged user
    @PostMapping("/deleteComment/{postId}/{commentId}")
    public String deleteComment(@PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId, HttpSession session) {
        //get session identity
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/notAuthorised";
        }
        BlogComment comment = commentService.getCommentById(commentId);
        //check if the comment belongs to the user:
        if (!Objects.equals(comment.getUser().getId(), user.getId())) {
            //niet geauthoriseerd: comment behoort niet tot de user
            return "redirect:/notAuthorised";
        } else {
            commentService.deleteCommentById(commentId);
        }
        return "redirect:/viewPost/" + postId;

    }
    //delete blogpost comment for a specific postid and check the identity of the logged user



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


    //method om individuele post te bekijken en de bijhorende comments in de vorm van pageable list en findCommentPaginated
    @GetMapping("/viewPost/{id}")
    public String viewPost(@PathVariable("id") Long id,
                           @RequestParam(name = "page", defaultValue = "1") int pageNo,
                           Model model, HttpSession session) {
        BlogPost post = postService.getPostById(id);
        model.addAttribute("post", post);

        // Alle comments toevoegen aan het model als pageable
        int pageSize = 5; // Set the number of comments per page
        Page<BlogComment> page = commentService.findCommentPaginated(id, pageNo, pageSize);
        List<BlogComment> commentList = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("commentList", commentList);

        model.addAttribute("newComment", new BlogComment());

        return "blogpostdetail";
    }

    // methode om nieuwe comment toe te voegen aan een post
    @PostMapping("/blog_post/{blogpostId}/blog_comment")
    public String createComment(@PathVariable(value = "blogpostId") Long postId,
                                HttpSession session, Model model,
                                @ModelAttribute("commentText") BlogComment newComment) {
        BlogPost post = postService.getPostById(postId);

        //User met post verbinden, of anders met anonymous
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            user = userService.findByUserName("anonymous").orElseThrow(() -> new IllegalArgumentException("Invalid username"));
        }
        newComment.setUser(user);
        newComment.setPost(post);

        commentService.saveComment(newComment);
        return "redirect:/viewPost/" + postId;
    }

    //get mapping to retrieve the number of posts and number of users in the database
    @GetMapping("/stats")
    public String getStats(Model model) {
        long postCount = postService.countAllBlogPosts();
        long userCount = userService.countUsers();
        model.addAttribute("postCount", postCount);
        model.addAttribute("userCount", userCount);
        return "blogcentral";
    }

    @GetMapping("/notAuthorised")
    public String notAuthorised() {
        return "notauthorised";
    }




    //PostMapping used in blogcentral.html to search blogpost description and content
        @GetMapping("/sendSearchString")
    public String sendSearchString(@RequestParam("searchString") String searchString, Model model) {
        List<BlogPost> searchResultList = postService.searchPostsByTitleDescriptionContentContaining(searchString);
        model.addAttribute("searchResultList", searchResultList);
        return "searchResults";
    }





    @PostMapping("/likePost/{id}")
    public String likePost(@PathVariable Long id, HttpSession session) {
        User sessionUser = (User) session.getAttribute("loggedInUser");
        if (sessionUser == null) {
            return "redirect:/login";
        }
        Optional<User> user = userRepository.findByUsername(sessionUser.getUsername());
        if (user.isPresent()) {
            postService.likeOrUnlikePost(id, user.get());
        }
        return "redirect:/viewPost/" + id;
    }
}

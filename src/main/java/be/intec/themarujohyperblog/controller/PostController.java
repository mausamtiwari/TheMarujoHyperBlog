package be.intec.themarujohyperblog.controller;

import be.intec.themarujohyperblog.model.BlogPost;
import be.intec.themarujohyperblog.model.User;
import be.intec.themarujohyperblog.service.PostServiceImpl;
import be.intec.themarujohyperblog.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

@Controller
public class PostController {

    private final UserServiceImpl userServiceImpl;
    private final HttpSession httpSession;
    private PostServiceImpl postService;

    @Autowired //Controller
    public  PostController(PostServiceImpl postService, UserServiceImpl userServiceImpl, HttpSession httpSession) {
        this.postService = postService;
        this.userServiceImpl = userServiceImpl;
        this.httpSession = httpSession;
    }

    @GetMapping("/")  //root, eerste pagina
    public String viewHomePage(Model model) {
        return findPostPaginated(1,model); //dit beperkt ons tot 1 pagina?


    }

    @GetMapping("/showNewPostForm") // dit toont de pagina om een
    // nieuwe blogpost te maken, maar de post wordt niet hier gesaved!!
    public String showNewPostForm(Model model) {
        BlogPost post = new BlogPost();
        model.addAttribute("post", post);
        return "new_post";
    }




    @PostMapping("/createPost") // dit maakt een nieuwe post aan
    public String createPost(@ModelAttribute("post") BlogPost post) {
        if (post.getDescription() == null || post.getDescription().trim().isEmpty()) {
            post.setDescription("No description available");
        }
        post.setCreatedAt(new Date());
        post.setUpdatedAt(new Date());
        User user = (User) httpSession.getAttribute("user"); //hier krijg je de user!

        //Check of de user in de sessie ingelogd is, anders nieuwe login::
        if (user == null) {
            // Handle the case where the user is null. For example, you can redirect to the login page.
            return "redirect:/login";
        }

        post.setUser(user);


        postService.savePost(post);

        return "redirect:/";
    }

    @GetMapping("/updatePost/{id}") //dit toont de pagina om een bestaande post te updaten
    public String showUpdatePostForm(@PathVariable(value = "id") Long postId,Model model) {

        BlogPost post = postService.getPost(postId);
        model.addAttribute("post", post);
        return "edit_post";
    }

    @PostMapping("/updatePost/{id}") // dit doet de eigenlijke update van de post
    public String updatePost(@PathVariable(value = "id") Long postId, @ModelAttribute("post") BlogPost post) {
        BlogPost existingPost = postService.getPost(postId);
        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        existingPost.setUpdatedAt(new Date());
        postService.savePost(existingPost);
        return "redirect:/";
    }

    @GetMapping("/deletePost/{id}")//verwijdert bestaande posts met id
    public String deletePost(@PathVariable(value = "id") Long postId) {
        postService.deletePost(postId);
        return "redirect:/";
    }

    @GetMapping("/page/{pageNo}")
    public String findPostPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 6; // Number of posts per page
        Page<BlogPost> page = postService.findPostPaginated(pageNo, pageSize);
        List<BlogPost> postList = page.getContent(); // Get list of posts from the page

        // Add pagination attributes to the model
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        // Add the list of posts to the model
        model.addAttribute("postList", postList);

        return "blogcentral";
    }
 



}


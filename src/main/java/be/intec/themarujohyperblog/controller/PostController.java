package be.intec.themarujohyperblog.controller;

import be.intec.themarujohyperblog.model.BlogPost;
import be.intec.themarujohyperblog.service.PostService;
import be.intec.themarujohyperblog.service.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Controller
public class PostController {

    private PostServiceImpl postService;

    @Autowired //Controller
    public  PostController(PostServiceImpl postService) {
        this.postService = postService;
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

        // Add author information
        model.addAttribute("userName", "Your User Name");
        //model.addAttribute("userAvatar", "path/to/your/avatar.jpg");

        return "bloghome"; // Render the bloghome.html template
    }



}


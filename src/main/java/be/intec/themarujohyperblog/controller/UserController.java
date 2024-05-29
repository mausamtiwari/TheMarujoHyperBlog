package be.intec.themarujohyperblog.controller;

//import be.intec.themarujohyperblog.config.PasswordMatchesValidator;

import be.intec.themarujohyperblog.model.BlogPost;
import be.intec.themarujohyperblog.model.User;
import be.intec.themarujohyperblog.service.PostServiceImpl;
import be.intec.themarujohyperblog.service.UserService;
import be.intec.themarujohyperblog.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
public class UserController {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    ConstraintValidatorContext context;
    ;

    // private final PasswordMatchesValidator passwordMatchesValidator = new PasswordMatchesValidator();

    private final UserServiceImpl userService;
    private final PostServiceImpl postService;


    @Autowired
    public UserController(UserServiceImpl userService, PostServiceImpl postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // TODO PasswordMatchesValidator bug fix
    // Password confirmation validation error .
   /* @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username, @Valid @ModelAttribute("user") User user, Model model, BindingResult result) {
        // System.out.println("Registering user: " + user);
        logger.info("Registering user: {}", user);

        // Check for validation errors
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> logger.warn("Validation error: {}", error));
            model.addAttribute("error", "Validation failed");
            return "redirect:/register";
        }

        Optional<User> existingUserName = userService.findByUserName(user.getUsername());
        if (existingUserName.isPresent()) {
            logger.warn("User already exists: {}", username);
            model.addAttribute("error", "Username already exists");
            return "redirect:/register";
        }

        Optional<User> existingEmail = userService.findUserByEmail(user.getEmail());
        if (existingEmail.isPresent()) {
            logger.warn("User already exists with email: {}", existingEmail);
            model.addAttribute("error", "Email already exists");
            return "redirect:/register";
        }

        // Check for password match
        if (!passwordMatchesValidator.isValid(user,null)) {
            model.addAttribute("error", "Passwords do not match");
            return "redirect:/register";
        }

        // Register user if all checks pass
        userService.registerUser(user);
        logger.info("User registered with username: {}", user.getUsername());
        return "redirect:/login";
    }


    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error, Model model) {
        model.addAttribute("user", new User());
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "login";
    }*/

    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username, @Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        logger.info("Registering user: {}", user);

        // Check for validation errors
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                model.addAttribute("error", error.getDefaultMessage());
                logger.warn("Validation error: field: {}, message: {}", error.getField(), error.getDefaultMessage());
                // Check if the error is related to the password field
                if ("password".equals(error.getField())) {
                    logger.warn("Password validation error: {}", error.getDefaultMessage());
                    model.addAttribute("passwordError", error.getDefaultMessage());
                }
            }
            model.addAttribute("user", user);
            return "register";
        }

        Optional<User> existingUserName = userService.findByUserName(user.getUsername());
        if (existingUserName.isPresent()) {
            logger.warn("User already exists: {}", username);
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        Optional<User> existingEmail = userService.findUserByEmail(user.getEmail());
        if (existingEmail.isPresent()) {
            logger.warn("User already exists with email: {}", existingEmail);
            model.addAttribute("error", "Email already exists");
            return "register";
        }

        // Check for password match
        boolean isValid = user.getPassword().equals(user.getConfirmPassword());
        if (!isValid) {
            logger.warn("Passwords do not match");
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }

        // Register user if all checks pass
        userService.registerUser(user);
        logger.info("User registered with username: {}", user.getUsername());
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error, Model model) {
        model.addAttribute("user", new User());
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model, HttpSession session) {
        logger.info("Attempting to login with username: {}", username);
        //System.out.println("Attempting to login with username: " + username);
        Optional<User> userOptional = userService.findByUserName(username);
        if (userOptional.isEmpty()) {
            logger.warn("User not found in database: {}", username);
            // System.out.println("Invalid login credentials.");
            model.addAttribute("error", "User not found in database");
            model.addAttribute("user", new User());
            return "login";
        }
        if (!password.equals(userOptional.get().getPassword())) {
            logger.warn("Invalid login credentials for username: {}", username);
            // System.out.println("Invalid login credentials.");
            model.addAttribute("error", "Invalid username or password");
            model.addAttribute("user", new User());
            return "login"; // Returning to the login page with the error message
        }
        User user = userOptional.get();
        logger.info("{} logged in successfully", username);
        session.setAttribute("username", username); // Store username in the session
        // System.out.println(username + " logged in successfully");
        session.setAttribute("loggedInUser", user);
         model.addAttribute("user", user);
        List<BlogPost> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);

        return "afterlogin";
    }

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return principal.getAttributes();
    }


   /*@PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        User user = userService.findByUserName(username).orElse(null);
        if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
        model.addAttribute("user", user);
        return "redirect:/blogcentral";
    }*/

  /*  @GetMapping("/myPosts")
    public String showMyPosts(Model model, @RequestParam(value = "page", defaultValue = "1") int page, @AuthenticationPrincipal User user) {
        int pageSize = 5; // Number of posts per page
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        Page<BlogPost> userPosts = postService.getPostsByUser(user, pageable);
        model.addAttribute("userPosts", userPosts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPosts.getTotalPages());

        return "userposts";
    }*/

    @GetMapping("/profile/{id}")
    public String viewUserProfile(@PathVariable("id") Long id, Model model) {
        Optional<User> optionalUser = userService.findUserById(id);
        if (optionalUser.isEmpty()) {
            logger.error("User with id {} not found", id);
            return "error";
        }
        model.addAttribute("user", optionalUser.get());
        return "profile";
    }


    @GetMapping("/edit/{id}")
    public String showEditProfileForm(@PathVariable("id") Long id, Model model) {
        Optional<User> optionalUser = userService.findUserById(id);
        if (optionalUser.isEmpty()) {
            logger.error("User with id {} not found", id);
            return "error";
        }
        model.addAttribute("user", optionalUser.get());
        return "edit-profile";
    }

    @PostMapping("/edit/{id}")
    public String editUserProfile(@PathVariable("id") Long id, @Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        logger.info("Attempting to update user profile: {}", user);

        // Check for validation errors
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                logger.warn("Validation error: field: {}, message: {}", error.getField(), error.getDefaultMessage());
                model.addAttribute("error", error.getDefaultMessage());
                if ("password".equals(error.getField())) {
                    logger.warn("Password validation error: {}", error.getDefaultMessage());
                    model.addAttribute("passwordError", error.getDefaultMessage());
                }
            }
            model.addAttribute("user", user);
            return "edit-profile";
        }

        // Retrieve the existing user from the database
        Optional<User> existingUser = userService.findUserById(id);
        if (existingUser.isEmpty()) {
            logger.error("User with id {} not found", id);
            model.addAttribute("error", "User not found.");
            return "error";
        }

        User updatedUser = existingUser.get();

        // Check if email is taken by another user
        if (!updatedUser.getEmail().equals(user.getEmail())) {
            Optional<User> existingEmail = userService.findUserByEmail(user.getEmail());
            if (existingEmail.isPresent()) {
                logger.warn("Email already exists: {}", user.getEmail());
                model.addAttribute("error", "Email already exists");
                model.addAttribute("user", user);
                return "edit-profile";
            }
        }

        // Check for password match
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            logger.warn("Passwords do not match");
            model.addAttribute("error", "Passwords do not match");
            model.addAttribute("user", user);
            return "edit-profile";
        }

        // Ensure mandatory fields are not left empty
        if (user.getFirstName().isEmpty() || user.getLastName().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty() || user.getConfirmPassword().isEmpty()) {
            logger.warn("One or more fields are empty");
            model.addAttribute("error", "All fields are required.");
            model.addAttribute("user", user);
            return "edit-profile";
        }

        logger.info("Updating user profile for: {}", user);
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setConfirmPassword(user.getConfirmPassword());
        logger.info("User profile updated: {}", user);

        userService.updateUser(updatedUser);
        return "redirect:/profile/" + id;
    }




    @PostMapping("/user/delete/{id}")
    public String deleteUserProfile(@PathVariable("id") Long id, Model model) {
        logger.info("Attempting to delete user with id: {}", id);
        Optional<User> optionalUser = userService.findUserById(id);
        if (optionalUser.isEmpty()) {
            logger.error("User with id {} not found", id);
            model.addAttribute("error", "User not found.");
            return "error";
        }
        try {
            userService.deleteUser(id);
            logger.info("User with id {} deleted successfully", id);
            return "blogcentral";
        } catch (Exception e) {
            logger.error("Error occurred while deleting user with id {}", id, e);
            model.addAttribute("error", "Error occurred while deleting user.");
            return "error";
        }
    }

    @PostMapping("/delete-picture/{id}")
    public String deleteProfilePicture(@PathVariable("id") Long id) {
        Optional<User> optionalUser = userService.findUserById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setProfilePicture(null);
            userService.registerUser(user);
        }
        return "redirect:/profile/" + id;
    }

    @PostMapping("/upload/{id}")
    public String uploadProfilePicture(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "redirect:/profile/" + id;
        }
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.write(path, bytes);

            Optional<User> optionalUser = userService.findUserById(id);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setProfilePicture(file.getOriginalFilename());
                userService.registerUser(user);
            }

        } catch (IOException e) {
            logger.error("Error occurred while uploading profile picture for user with id {}", id, e);
        }
        return "redirect:/profile/" + id;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            logger.info("{} logged out successfully", username);
        } else {
            logger.info("User logged out successfully");
        }
        session.invalidate();
        return "redirect:/login?logout";
    }


}

package be.intec.themarujohyperblog.controller;

import be.intec.themarujohyperblog.model.BlogPost;
import be.intec.themarujohyperblog.model.User;
import be.intec.themarujohyperblog.service.PostServiceImpl;
import be.intec.themarujohyperblog.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    //private final BCryptPasswordEncoder passwordEncoder;


    private final UserServiceImpl userService;
    private final PostServiceImpl postService;


    @Autowired
    public UserController(UserServiceImpl userService, PostServiceImpl postService) {
        //this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(@RequestParam(value = "error", required = false) String error, Model model) {
        model.addAttribute("user", new User());
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        logger.info("Registering user: {}", user);

        String validationResult = validateUser(user, result, model);
        if (validationResult != null) {
            return "register";
        }

        String checkResult = checkUsernameAndEmail(user, model);
        if (checkResult != null) {
            return "register";
        }

       /*// Encode the password before saving the user
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        logger.debug("Raw password during registration: {}", user.getPassword());
        logger.debug("Encoded password during registration: {}", hashedPassword);
        user.setPassword(hashedPassword);*/

    // Register user if all checks pass
        userService.registerUser(user);
        logger.info("User registered with username: {}", user.getUsername());
        return "redirect:/login";
    }
  /*  @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, @RequestParam("profilePicture") MultipartFile profilePicture, Model model) {
        logger.info("Registering user: {}", user);

        String validationResult = validateUser(user, result, model);
        if (validationResult != null) {
            return "register";
        }

        String checkResult = checkUsernameAndEmail(user, model);
        if (checkResult != null) {
            return "register";
        }

        // Handle file upload
        if (!profilePicture.isEmpty()) {
            String uploadError = handleFileUpload(profilePicture, user);
            if (uploadError != null) {
                model.addAttribute("error", uploadError);
                return "register";
            }
        }

        // Register user if all checks pass
        userService.registerUser(user);
        logger.info("User registered with username: {}", user.getUsername());
        return "redirect:/login";
    }*/

    @PostMapping("/upload/{id}")
    public String uploadProfilePicture(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file, HttpSession session) {
        if (file.isEmpty()) {
            return "redirect:/profile/" + id;
        }
        try {
            Optional<User> optionalUser = userService.findUserById(id);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                String uploadError = handleFileUpload(file, user);
                if (uploadError == null) {
                    userService.registerUser(user);
                    session.setAttribute("loggedInUser", user);
                } else {
                    logger.error(uploadError);
                }
            }
        } catch (Exception e) {
            logger.error("Error occurred while uploading profile picture for user with id {}", id, e);
        }
        return "redirect:/profile/" + id;
    }

    private String handleFileUpload(MultipartFile file, User user) {
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.write(path, bytes);
            user.setProfilePicture(file.getOriginalFilename());
            return null; // No error
        } catch (IOException e) {
            logger.error("Error uploading profile picture", e);
            return "Failed to upload profile picture. Please try again.";
        }
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
        Optional<User> userOptional = userService.findByUserName(username);
        if (userOptional.isEmpty()) {
            logger.warn("User not found in database: {}", username);
            model.addAttribute("error", "User not found in database");
            model.addAttribute("user", new User());
            return "login";
        }
        User user = userOptional.get();
        /*logger.debug("Raw password: {}", password);
        logger.debug("Encoded password from DB: {}", user.getPassword());
        boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
        logger.debug("Password matches: {}", passwordMatches);
*/
        //if (passwordEncoder.matches(password, user.getPassword())) {
        if (password.equals(user.getPassword())) {
            logger.info("{} logged in successfully", username);
            session.setAttribute("username", username);
            session.setAttribute("loggedInUser", user);
            model.addAttribute("user", user);
            List<BlogPost> posts = postService.getAllPosts();
            model.addAttribute("posts", posts);

            return "afterlogin";
        }
        logger.warn("Invalid login credentials for username: {}", username);
        model.addAttribute("error", "Invalid username or password");
        model.addAttribute("user", new User());
        return "login";

    }


    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return principal.getAttributes();
    }


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

        String validationResult = validateUser(user, result, model);
        if (validationResult != null) {
            return "edit-profile";
        }

        // Retrieve the existing user from the database
        Optional<User> existingUserOptional = userService.findUserById(id);
        if (existingUserOptional.isEmpty()) {
            logger.error("User with id {} not found", id);
            model.addAttribute("error", "User not found.");
            return "error";
        }

        User existingUser = existingUserOptional.get();

        // Check if email is taken by another user
        if (!existingUser.getEmail().equals(user.getEmail())) {
            Optional<User> existingEmail = userService.findUserByEmail(user.getEmail());
            if (existingEmail.isPresent()) {
                logger.warn("Email already exists: {}", user.getEmail());
                model.addAttribute("error", "Email already exists");
                model.addAttribute("user", user);
                return "edit-profile";
            }
        }

        logger.info("Updating user profile for: {}", user);
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        //existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        //existingUser.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
        existingUser.setPassword(user.getPassword());
        existingUser.setConfirmPassword(user.getConfirmPassword());
        logger.info("User profile updated: {}", user);

        userService.updateUser(existingUser);
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


    // Validation for register and update methods.

    private String validateUser(User user, BindingResult result, Model model) {
        // Check for validation errors
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                logger.warn("Validation error: field: {}, message: {}", error.getField(), error.getDefaultMessage());
                model.addAttribute("error", error.getDefaultMessage());
                /*if ("password".equals(error.getField())) {
                    logger.warn("Password validation error: {}", error.getDefaultMessage());
                   // model.addAttribute("passwordError", error.getDefaultMessage());
                }*/
            }
            model.addAttribute("user", user);
            return "error";
        }

        // Ensure mandatory fields are not left empty
        if (user.getFirstName().isEmpty() || user.getLastName().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty() || user.getConfirmPassword().isEmpty()) {
            logger.warn("One or more fields are empty");
            model.addAttribute("error", "All fields are required.");
            model.addAttribute("user", user);
            return "error";
        }

        // Check for password match
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            logger.warn("Passwords do not match");
            model.addAttribute("error", "Passwords do not match");
            model.addAttribute("user", user);
            return "error";
        }

        return null;
    }

    private String checkUsernameAndEmail(User user, Model model) {
        // Check if username is taken
        Optional<User> existingUserName = userService.findByUserName(user.getUsername());
        if (existingUserName.isPresent()) {
            logger.warn("User already exists with username: {}", user.getUsername());
            model.addAttribute("error", "Username already exists");
            return "error";
        }

        // Check if email is taken
        Optional<User> existingEmail = userService.findUserByEmail(user.getEmail());
        if (existingEmail.isPresent()) {
            logger.warn("User already exists with email: {}", user.getEmail());
            model.addAttribute("error", "Email already exists");
            return "error";
        }

        return null;
    }


}

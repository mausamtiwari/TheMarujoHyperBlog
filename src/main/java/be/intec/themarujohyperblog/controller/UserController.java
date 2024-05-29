package be.intec.themarujohyperblog.controller;

//import be.intec.themarujohyperblog.config.PasswordMatchesValidator;

import be.intec.themarujohyperblog.model.User;
import be.intec.themarujohyperblog.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;


@Controller
public class UserController {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    ConstraintValidatorContext context;
    ;

    // private final PasswordMatchesValidator passwordMatchesValidator = new PasswordMatchesValidator();

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Password confirmation validation error
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
            return "/register";
        }

        Optional<User> existingEmail = userService.findUserByEmail(user.getEmail());
        if (existingEmail.isPresent()) {
            logger.warn("User already exists with email: {}", existingEmail);
            model.addAttribute("error", "Email already exists");
            return "/register";
        }

        // Check for password match
        boolean isValid = user.getPassword().equals(user.getConfirmPassword());
        if (!isValid) {
            logger.warn("Passwords do not match");
            model.addAttribute("error", "Passwords do not match");
            return "/register";
        }

        // Register user if all checks pass
        userService.registerUser(user);
        logger.info("User registered with username: {}", user.getUsername());
        return "/login";
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
        if (userOptional.isEmpty() || (!password.equals(userOptional.get().getPassword()))) {
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
        model.addAttribute("user", user);
        return "afterlogin";
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
        if (result.hasErrors()) {
            return "edit-profile";
        }
        Optional<User> existingUser = userService.findUserById(id);
        if (existingUser.isEmpty()) {
            model.addAttribute("error", "User not found.");
            return "error";
        }

        User updatedUser = existingUser.get();
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setConfirmPassword(user.getConfirmPassword());

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
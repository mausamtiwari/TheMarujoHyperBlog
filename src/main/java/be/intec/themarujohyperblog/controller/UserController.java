package be.intec.themarujohyperblog.controller;

import be.intec.themarujohyperblog.model.User;
import be.intec.themarujohyperblog.service.UserService;
import jakarta.validation.Valid;
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

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, Model model) {
        System.out.println("Registering user: " + user);
        Optional<User> existingUser = userService.findByUserName(user.getUsername());
        if (existingUser.isPresent()) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        userService.registerUser(user);
        System.out.println("User registered with username: " + user.getUsername());
        return "redirect:/login";
    }
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    /*@PostMapping("/login")
    public String login(@ModelAttribute User user) {
        Optional<User> userAuth = userService.findByUserNameAndPassword(user.getUsername(), user.getPassword());
        System.out.println(userAuth);
        if (Objects.nonNull(userAuth)) {
            return "redirect:/";
        } else {
            return "redirect:/login";
        }

    }*/



    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        System.out.println("Attempting to login with username: " + username);
        Optional<User> userOptional = userService.findByUserName(username);
        if (userOptional.isEmpty()) {
            System.out.println("User not found");
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
        User user = userOptional.get();
        if (!password.equals(user.getPassword())) {
            System.out.println("Password does not match");
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
        System.out.println("User logged in successfully");
        model.addAttribute("user", user);
        return "blogcentral";
    }


   /* @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        User user = userService.findByUserName(username).orElse(null);
        if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
        model.addAttribute("user", user);
        return "redirect:/blogcentral";
    }*/

    /*@GetMapping("/profile/{id}")
    public String viewUserProfile(@PathVariable("id") Long id, Model model) {
        Optional<User> optionalUser = userService.findUserById(id);
        if (optionalUser.isEmpty()) {
            return "error";
        }
        model.addAttribute("user", optionalUser.get());
        return "profile";
    }


    @GetMapping("/edit/{id}")
    public String showEditProfileForm(@PathVariable("id") Long id, Model model) {
        Optional<User> optionalUser = userService.findUserById(id);
        if (optionalUser.isEmpty()) {
            return "error";
        }
        model.addAttribute("user", optionalUser.get());
        return "edit-profile";
    }

    @PostMapping("/edit/{id}")
    public String editUserProfile(@PathVariable("id") Long id, @Valid @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "edit-profile";
        }
        user.setId(id);
        userService.registerUser(user);
        return "redirect:/profile/" + id;
    }

    @PostMapping("/user/delete/{id}")
    public String deleteUserProfile(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/";
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
            e.printStackTrace();
        }
        return "redirect:/profile/" + id;
    }*/
}

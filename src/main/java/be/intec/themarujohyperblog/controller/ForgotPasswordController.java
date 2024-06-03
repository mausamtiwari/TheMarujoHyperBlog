package be.intec.themarujohyperblog.controller;

import be.intec.themarujohyperblog.model.ForgotPasswordToken;
import be.intec.themarujohyperblog.model.User;
import be.intec.themarujohyperblog.repository.ForgotPasswordRepository;
import be.intec.themarujohyperblog.service.ForgotPasswordService;
import be.intec.themarujohyperblog.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Controller
public class ForgotPasswordController {

    private final UserService userService;
    private final ForgotPasswordService forgotPasswordService;
    private final ForgotPasswordRepository forgotPasswordRepository;

   // private final BCryptPasswordEncoder passwordEncoder;




    @Autowired
    public ForgotPasswordController(UserService userService, ForgotPasswordService forgotPasswordService, ForgotPasswordRepository forgotPasswordRepository) {
        this.userService = userService;
        this.forgotPasswordService = forgotPasswordService;
        this.forgotPasswordRepository = forgotPasswordRepository;
        //this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/password-request")
    public String passwordRequest() {
        return "password-request";
    }

    @PostMapping("/password-request")
    public String savePasswordRequest(@RequestParam("usernameOrEmail") String usernameOrEmail, Model model) {
        Optional<User> user = userService.findByUserName(usernameOrEmail);
        if (user.isEmpty()) {
            user = userService.findUserByEmail(usernameOrEmail);
            if (user.isEmpty()) {
                model.addAttribute("error", "User not found");
                return "password-request";
            }
        }

        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
        forgotPasswordToken.setExpireTime(forgotPasswordService.expireTimeRange());
        forgotPasswordToken.setToken(forgotPasswordService.generateToken(usernameOrEmail));
        forgotPasswordToken.setUser(user.get());
        forgotPasswordToken.setUsed(false);

        forgotPasswordRepository.save(forgotPasswordToken);

        String emailLink = "http://localhost:8080/password-reset?token=" + forgotPasswordToken.getToken();
        try {
            forgotPasswordService.sendEmail(user.get().getEmail(), "Password Reset Link", emailLink, user.get().getUsername());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email");
            return "password-request";
        }
        return "redirect:/password-request?success";
    }

    @GetMapping("/password-reset")
    public String passwordReset(@RequestParam("token") String token, Model model) {
        ForgotPasswordToken forgotPasswordToken = forgotPasswordRepository.findByToken(token);
        if (forgotPasswordToken == null || forgotPasswordToken.isUsed()) {
            model.addAttribute("error", "Invalid or expired token");
            return "password-reset";
        }
        model.addAttribute("token", token);
        return "password-reset";
    }

    @PostMapping("/password-reset")
    public String savePasswordReset(@RequestParam("token") String token,
                                    @RequestParam("password") String password,
                                    @RequestParam("confirmPassword") String confirmPassword,
                                    Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "password-reset";
        }

        ForgotPasswordToken forgotPasswordToken = forgotPasswordRepository.findByToken(token);
        if (forgotPasswordToken == null || forgotPasswordToken.isUsed()) {
            model.addAttribute("error", "Invalid or expired token");
            return "password-reset";
        }

        // Validate the new password before updating the user
        if (!password.matches("^(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{1,10}$")) {
            model.addAttribute("error", "Password must contain at least one capital letter, one special character, letters, and numbers, and must be a maximum of 10 characters long.");
            return "password-reset";
        }

        try {
            User user = forgotPasswordToken.getUser();
            user.setPassword(password);
           // user.setPassword(passwordEncoder.encode(password));
            userService.updateUser(user);

            forgotPasswordToken.setUsed(true);
            forgotPasswordRepository.save(forgotPasswordToken);
        } catch (ConstraintViolationException e) {
            model.addAttribute("error", e.getConstraintViolations().iterator().next().getMessage());
            return "password-reset";
        }

        return "redirect:/login?resetSuccess";
    }

}

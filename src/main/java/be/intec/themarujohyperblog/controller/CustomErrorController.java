package be.intec.themarujohyperblog.controller;

import be.intec.themarujohyperblog.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController {
    HttpSession session;
    // Handles all errors by mapping to "/error"
    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Retrieves the HTTP status code from the request
        Object status = request.getAttribute("javax.servlet.error.status_code");
        // Default error message
        String errorMessage = "Unexpected error";

        if (status != null) {
            // Converts the status object  to int
            int statusCode = (int) status;
            {
                User user = (User) session.getAttribute("loggedInUser");
                if (user != null) {
                    if (statusCode == 500 || statusCode == 404 || statusCode == 400 || statusCode == 403 || statusCode == 401 || statusCode == 405) {
                        // Retrieves the exception that caused the error
                        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
                        if (throwable != null) {
                            // If an exception is found, use its message as the error message
                            errorMessage = throwable.getMessage();
                        }
                    }
                }
            }

        }
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }

}
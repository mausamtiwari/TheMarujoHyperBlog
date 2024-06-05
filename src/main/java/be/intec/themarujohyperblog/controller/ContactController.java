package be.intec.themarujohyperblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactController {

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/contact")
    public String contact() {
        return "contact"; // Returns the contact.html template
    }

    @PostMapping("/sendEmail")
    public String sendEmail(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message) {

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo("mausamtiwari93@gmail.com");
        emailMessage.setSubject(subject);
        emailMessage.setText("Name: " + name + "\nEmail: " + email + "\n\nMessage:\n" + message);

        mailSender.send(emailMessage);

        return "redirect:/contact?success";
    }
}

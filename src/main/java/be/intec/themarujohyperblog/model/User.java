package be.intec.themarujohyperblog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Entity
@Data
public class User {

    public interface CreateGroup extends Default {
    }

    public interface UpdateGroup extends Default {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required", groups = {CreateGroup.class, UpdateGroup.class})
    private String firstName;

    @NotBlank(message = "Last name is required", groups = {CreateGroup.class, UpdateGroup.class})
    private String lastName;

    @Column(unique = true)
    @NotBlank(message = "Username is required", groups = {CreateGroup.class, UpdateGroup.class})
    private String username;

    @NotBlank(message = "Password is required", groups = {CreateGroup.class})
    /*@Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{1,10}$",
            message = "Password must contain at least one capital letter, one special character, letters, and numbers, and must be a maximum of 10 characters long.",
            groups = {CreateGroup.class, UpdateGroup.class}
    )*/
    @Column(length = 60)
    private String password;

    @Transient
    @NotBlank(message = "Confirm Password is required", groups = {CreateGroup.class})
    private String confirmPassword;

    @NotBlank(message = "Email is required", groups = {CreateGroup.class, UpdateGroup.class})
    @Column(unique = true)
    @Email(message = "Email should be valid", groups = {CreateGroup.class, UpdateGroup.class})
    private String email;

    private String profilePicture;

    @Column(nullable = false)
    private boolean enabled = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlogPost> blogPosts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlogComment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

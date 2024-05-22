package be.intec.themarujohyperblog.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "blog_comment")
public class BlogComment extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Lob
    private String text;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "blog_post_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private BlogPost blogPost;

    // Default constructor
    public BlogComment() {}

    // Constructor with all fields
    public BlogComment(Long id, @NotNull String text, BlogPost blogPost) {
        this.id = id;
        this.text = text;
        this.blogPost = blogPost;
    }

    // Constructor without id
    public BlogComment(@NotNull String text, BlogPost blogPost) {
        this.text = text;
        this.blogPost = blogPost;
    }

    // Constructor with text only
    public BlogComment(@NotNull String text) {
        this.text = text;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public BlogPost getBlogPost() {
        return blogPost;
    }

    public void setBlogPost(BlogPost blogPost) {
        this.blogPost = blogPost;
    }
}

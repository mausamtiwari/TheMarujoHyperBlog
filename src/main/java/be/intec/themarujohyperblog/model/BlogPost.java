package be.intec.themarujohyperblog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "blog_post")
public class BlogPost extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String title;

    /* Description voorlopig weggelaten. In versie 2.0 kan dit dienen voor tags
    @NotNull
    @Column(nullable = true) // description is niet verplicht
    private String description;

     */


    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;


    @ManyToOne(fetch = FetchType.LAZY, optional = false) //To-check:  relatie OTM or MTO?
    @JoinColumn(name = "user_id", nullable = false)
    private User user;



    // Default constructor
    public BlogPost() {
    }

    // Constructor with all fields
    public BlogPost(Long id, @NotNull String title, String content, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
    }

    // Constructor without id
    public BlogPost(@NotNull String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

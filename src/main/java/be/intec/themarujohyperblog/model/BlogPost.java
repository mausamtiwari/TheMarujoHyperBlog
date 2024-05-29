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



    @Column(nullable = true) // description toegevoegd, anders is het moeilijk een search te doen op inhoud.
    private String description;




    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;


    @ManyToOne(fetch = FetchType.LAZY, optional = false) //To-check:  relatie OTM or MTO?
    @JoinColumn(name = "user_id", nullable = false)
    private User user;



    // Default constructor
    public BlogPost() {
    }

    public BlogPost(Long id, String title, String description, String content) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.content = content;
    }

    // Constructor with all fields
    public BlogPost(Long id, @NotNull String title, String description, String content, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
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

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

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

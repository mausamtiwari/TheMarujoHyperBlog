package be.intec.themarujohyperblog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String token;


    // Each user may have many tokens as they may forget passwords many times
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime expireTime;

    @Column(nullable = false)
    private boolean isUsed;
}

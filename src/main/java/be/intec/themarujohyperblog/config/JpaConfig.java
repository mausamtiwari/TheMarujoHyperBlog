package be.intec.themarujohyperblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


// Because createdAt was always null. To avoid null createdAt
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}

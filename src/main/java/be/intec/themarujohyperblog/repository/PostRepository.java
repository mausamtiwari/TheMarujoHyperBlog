package be.intec.themarujohyperblog.repository;

import be.intec.themarujohyperblog.model.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<BlogPost, Long> {

}

package be.intec.themarujohyperblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import be.intec.themarujohyperblog.model.BlogPost;

@Repository
public interface PostRepository extends JpaRepository<BlogPost,Long> {

}

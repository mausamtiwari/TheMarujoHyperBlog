package be.intec.themarujohyperblog.repository;

import be.intec.themarujohyperblog.model.BlogComment;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface
CommentRepository extends JpaRepository<BlogComment,Long> {
    Page<BlogComment> findByPostId(Long postId, Pageable pageable);

    List<BlogComment> findByPostId(Long postId);

    Optional<BlogComment> findByIdAndPostId(Long id, Long postId);
    Optional<BlogComment> findById(Long id);
}
package be.intec.themarujohyperblog.service;

import be.intec.themarujohyperblog.model.BlogComment;
import be.intec.themarujohyperblog.repository.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    @Override
    public List<BlogComment> getAllComment() {
        return commentRepository.findAll();
    }
    @Override
    public void saveComment(BlogComment comment) {
        this.commentRepository.save(comment);
    }
    @Override
    public BlogComment findCommentById(Long id) {
        Optional<BlogComment> comment = commentRepository.findById(id);
        return comment.orElseThrow(() -> new RuntimeException("Comment not found for id :: " + id));
    }
    @Override
    public void deleteComment(Long id) {
        boolean exists = commentRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Comment id " + id + " not found");
        }
        commentRepository.deleteById(id);
    }

    @Override
    public List<BlogComment> findCommentByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Override
    public Page<BlogComment> findCommentPaginated(Long postId, int pageNo, int pageSize) {
        // Creates a Pageable object with the given page number (pageNo) and page size (pageSize)
        // PageRequest.of(pageNo-1, pageSize) creates a Pageable instance, where pageNo-1 adjusts the page number to be zero-based.
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        // Uses the commentRepository to find comments by the postId with pagination
        // The method findByPostId takes the postId and pageable as arguments and returns a Page<Comment> containing the comments for that page.
        return commentRepository.findByPostId(postId, pageable);
    }

    public BlogComment findById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }
}
package be.intec.themarujohyperblog.service;

import be.intec.themarujohyperblog.model.BlogComment;
import be.intec.themarujohyperblog.repository.CommentRepository;
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
    public BlogComment getCommentById(Long id) {
        Optional<BlogComment> comment = commentRepository.findById(id);
        return comment.orElseThrow(() -> new RuntimeException("Comment not found for id :: " + id));
    }
    @Override
    public void deleteCommentById(Long id) {
        this.commentRepository.deleteById(id);
    }
}
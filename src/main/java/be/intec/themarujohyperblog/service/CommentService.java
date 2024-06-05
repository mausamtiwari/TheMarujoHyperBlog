package be.intec.themarujohyperblog.service;

import be.intec.themarujohyperblog.model.BlogComment;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentService {
    List<BlogComment> getAllComment();
    void saveComment(BlogComment comment);
    BlogComment findCommentById(Long id);
    void deleteComment(Long commentId );
    BlogComment getCommentById(Long id);
    void deleteCommentById(Long commentId );
    List<BlogComment> findCommentByPostId(Long postId);
    Page<BlogComment> findCommentPaginated(Long postId, int pageNo, int pageSize);
}


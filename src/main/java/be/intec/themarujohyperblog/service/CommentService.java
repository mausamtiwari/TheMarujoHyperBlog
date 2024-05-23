package be.intec.themarujohyperblog.service;

import be.intec.themarujohyperblog.model.BlogComment;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentService {

    List<BlogComment> getAllComment();
    void saveComment(BlogComment comment);
    BlogComment getCommentById(Long id);
    void deleteCommentById(Long id);
    Page<BlogComment> findCommentPaginated(Long postId, int pageNo, int pageSize);
}

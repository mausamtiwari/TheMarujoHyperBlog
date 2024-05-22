package be.intec.themarujohyperblog.service;

import be.intec.themarujohyperblog.model.BlogComment;

import java.util.List;

public interface CommentService {
    List<BlogComment> getAllComment();
    void saveComment(BlogComment comment);
    BlogComment getCommentById(Long id);
    void deleteCommentById(Long id);
}


package be.intec.themarujohyperblog.service;

import be.intec.themarujohyperblog.model.BlogPost;
import be.intec.themarujohyperblog.model.Like;
import be.intec.themarujohyperblog.model.User;
import be.intec.themarujohyperblog.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeServiceImpl {
    private final LikeRepository likeRepository;

    @Autowired
    public LikeServiceImpl(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public Like saveLike(Like like) {
        return likeRepository.save(like);
    }

    public void deleteLike(Long id) {
        likeRepository.deleteById(id);
    }

    public Optional<Like> findByPostAndUser(BlogPost post, User user) {
        return likeRepository.findByPostAndUser(post, user);
    }

    public boolean toggleLike(BlogPost post, User user) {
        Optional<Like> existingLike = findByPostAndUser(post, user);
        if (existingLike.isPresent()) {
            deleteLike(existingLike.get().getId());
            return false; // Unliked
        } else {
            Like like = new Like();
            like.setPost(post);
            like.setUser(user);
            saveLike(like);
            return true; // Liked
        }
    }

}


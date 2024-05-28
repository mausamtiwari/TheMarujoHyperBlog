package be.intec.themarujohyperblog.service;

import be.intec.themarujohyperblog.model.Like;
import be.intec.themarujohyperblog.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}


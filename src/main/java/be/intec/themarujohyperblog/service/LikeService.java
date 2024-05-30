package be.intec.themarujohyperblog.service;

import be.intec.themarujohyperblog.model.Like;


public interface LikeService {
    Like saveLike(Like like);
    void deleteLike(Long id);
}

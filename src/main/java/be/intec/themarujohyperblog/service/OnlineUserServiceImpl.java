package be.intec.themarujohyperblog.service;

import be.intec.themarujohyperblog.model.OnlineUser;
import be.intec.themarujohyperblog.repository.OnlineUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OnlineUserServiceImpl {

    @Autowired
    private OnlineUserRepository onlineUserRepository;

    public long countOnlineUsers() {
        return onlineUserRepository.count();
    }


    public OnlineUser addOnlineUser(String username) {
        OnlineUser user = new OnlineUser();
        user.setUsername(username);
        user.setLoginTime(LocalDateTime.now());
        onlineUserRepository.save(user);
        return user;
    }

    public boolean deleteOnlineUser(String username) {
        Optional<OnlineUser> user = onlineUserRepository.findByUsername(username);
        user.ifPresent(onlineUserRepository::delete);
        return false;
    }

    public List<OnlineUser> getAllOnlineUsers() {
        List<OnlineUser> allUsers = onlineUserRepository.findAll();
        // Filter out the online users based on some condition
        return allUsers.stream()
                .filter(OnlineUser::isOnline)
                .collect(Collectors.toList());
    }

    public OnlineUser updateOnlineUser(Long id, OnlineUser onlineUser) {
        Optional<OnlineUser> optionalUser = onlineUserRepository.findById(id);
        if (optionalUser.isPresent()) {
            OnlineUser existingUser = optionalUser.get();
            existingUser.setUsername(onlineUser.getUsername());
            existingUser.setLoginTime(LocalDateTime.now()); // Update login time or any other information as needed
            return onlineUserRepository.save(existingUser);
        } else {
            // Handle case where the user with the given id is not found
            return null;
        }
    }

    public OnlineUser getOnlineUserById(Long id) {
        Optional<OnlineUser> optionalUser = onlineUserRepository.findById(id);
        return optionalUser.orElse(null);
    }

}
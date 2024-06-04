package be.intec.themarujohyperblog.service;

import be.intec.themarujohyperblog.model.OnlineUser;
import be.intec.themarujohyperblog.model.Visitor;

import java.util.List;


public interface OnlineUserService {

    // Methods for managing online users
    List<OnlineUser> getAllOnlineUsers();
    OnlineUser getOnlineUserById(Long id);
    OnlineUser addOnlineUser(OnlineUser onlineUser);
    OnlineUser updateOnlineUser(Long id, OnlineUser onlineUser);
    boolean deleteOnlineUser(Long id);

    // Methods for managing visitors
    List<Visitor> getAllVisitors();
    void addVisitor();
}

package be.intec.themarujohyperblog.controller;

    import be.intec.themarujohyperblog.model.OnlineUser;
    import be.intec.themarujohyperblog.service.OnlineUserService;
    import be.intec.themarujohyperblog.service.OnlineUserServiceImpl;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.*;
    import java.util.List;

    @Controller
    @RequestMapping("/api/online-users")
    public class OnlineUserController {

        private final OnlineUserServiceImpl onlineUserService;

        @Autowired
        public OnlineUserController(OnlineUserServiceImpl onlineUserService) {
            this.onlineUserService = onlineUserService;
        }

        // Endpoint to get all online users
        @GetMapping
        public ResponseEntity<List<OnlineUser>> getAllOnlineUsers() {
            List<OnlineUser> onlineUsers = onlineUserService.getAllOnlineUsers();
            return ResponseEntity.ok(onlineUsers);
        }

        // Endpoint to get a specific online user by ID
        @GetMapping("/{id}")
        public ResponseEntity<OnlineUser> getOnlineUserById(@PathVariable("id") Long id) {
            OnlineUser onlineUser = onlineUserService.getOnlineUserById(id);
            if (onlineUser != null) {
                return ResponseEntity.ok(onlineUser);
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        // Endpoint to add a new online user
        @PostMapping
        public ResponseEntity<OnlineUser> addOnlineUser(@RequestBody OnlineUser onlineUser) {
            OnlineUser createdOnlineUser = onlineUserService.addOnlineUser(String.valueOf(onlineUser));
            return new ResponseEntity<>(createdOnlineUser, HttpStatus.CREATED);
        }

        // Endpoint to update an existing online user
        @PutMapping("/{id}")
        public ResponseEntity<OnlineUser> updateOnlineUser(@PathVariable("id") Long id, @RequestBody OnlineUser onlineUser) {
            OnlineUser updatedOnlineUser = onlineUserService.updateOnlineUser(id, onlineUser);
            if (updatedOnlineUser != null) {
                return ResponseEntity.ok(updatedOnlineUser);
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        // Endpoint to delete an online user by ID
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteOnlineUser(@PathVariable("id") Long id) {
            boolean deleted = onlineUserService.deleteOnlineUser(String.valueOf(id));
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }



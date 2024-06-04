package be.intec.themarujohyperblog.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;


    @Entity
    public class OnlineUser {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String username;

        private LocalDateTime loginTime;

        public OnlineUser() {
        }

        public OnlineUser(Long id, String username, LocalDateTime loginTime) {
            this.id = id;
            this.username = username;
            this.loginTime = loginTime;
        }


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public LocalDateTime getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(LocalDateTime loginTime) {
            this.loginTime = loginTime;
        }

        public boolean isOnline() {
            LocalDateTime currentTime = LocalDateTime.now();
            // Assuming a user is considered online if they logged in within the last 5 minutes
            LocalDateTime threshold = currentTime.minusMinutes(5);
            return loginTime.isAfter(threshold);
        }

        @Override
        public String toString() {
            return "OnlineUser{" +
                    "id=" + id +
                    ", username='" + username + '\'' +
                    ", loginTime=" + loginTime +
                    '}';
        }
// Getters and setters
    }

package be.intec.themarujohyperblog.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

    @Entity
    public class Visitor {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private LocalDateTime visitTime;

        public Visitor() {
        }

        public Visitor(Long id, LocalDateTime visitTime) {
            this.id = id;
            this.visitTime = visitTime;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public LocalDateTime getVisitTime() {
            return visitTime;
        }

        public void setVisitTime(LocalDateTime visitTime) {
            this.visitTime = visitTime;
        }
// Getters and setters
    }

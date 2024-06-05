package be.intec.themarujohyperblog.repository;

import be.intec.themarujohyperblog.model.OnlineUser;
import be.intec.themarujohyperblog.model.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    long count();
}
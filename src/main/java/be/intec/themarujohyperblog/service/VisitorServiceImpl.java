package be.intec.themarujohyperblog.service;

import be.intec.themarujohyperblog.model.Visitor;
import be.intec.themarujohyperblog.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VisitorServiceImpl {


    @Autowired
    private VisitorRepository visitorRepository;

    public long countVisitors() {
        return visitorRepository.count();
    }
    public void addVisitor() {
        Visitor visitor = new Visitor();
        visitor.setVisitTime(LocalDateTime.now());
        visitorRepository.save(visitor);
    }
    public List<Visitor> getAllVisitors() {
        return visitorRepository.findAll();
    }
}

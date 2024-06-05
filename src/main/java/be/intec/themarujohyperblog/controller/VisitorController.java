package be.intec.themarujohyperblog.controller;

import be.intec.themarujohyperblog.model.Visitor;
import be.intec.themarujohyperblog.service.VisitorService;
import be.intec.themarujohyperblog.service.VisitorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/visitors")
public class VisitorController {

    private final VisitorServiceImpl visitorService;

    @Autowired
    public VisitorController(VisitorServiceImpl visitorService) {
        this.visitorService = visitorService;
    }

    @GetMapping
    public ResponseEntity<List<Visitor>> getAllVisitors() {
        List<Visitor> visitors = visitorService.getAllVisitors();
        return ResponseEntity.ok(visitors);
    }

    @PostMapping
    public ResponseEntity<Void> addVisitor() {
        visitorService.addVisitor();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countVisitors() {
        long count = visitorService.countVisitors();
        return ResponseEntity.ok(count);
    }
}

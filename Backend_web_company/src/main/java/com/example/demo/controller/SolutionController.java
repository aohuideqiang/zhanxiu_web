package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Solution;
import com.example.demo.service.SolutionService;

@RestController
@RequestMapping("/api/solutions")
@CrossOrigin(origins = "*")
public class SolutionController {

    private final SolutionService solutionService;

    public SolutionController(SolutionService solutionService) {
        this.solutionService = solutionService;
    }

    @GetMapping
    public List<Solution> getAllSolutions() {
        return solutionService.list();
    }

    @GetMapping("/{id}")
    public Solution getSolutionById(@PathVariable Long id) {
        return solutionService.getById(id);
    }

    @PostMapping
    public boolean createSolution(@RequestBody Solution solution) {
        return solutionService.save(solution);
    }

    @PutMapping("/{id}")
    public boolean updateSolution(@PathVariable Long id, @RequestBody Solution solution) {
        solution.setId(id);
        return solutionService.saveOrUpdate(solution);
    }

    @DeleteMapping("/{id}")
    public boolean deleteSolution(@PathVariable Long id) {
        return solutionService.removeById(id);
    }
}

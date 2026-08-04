package com.example.demo.controller;

import cn.dev33.satoken.util.SaResult;
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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/solutions")
@CrossOrigin(origins = "*")
public class SolutionController {

    private final SolutionService solutionService;

    public SolutionController(SolutionService solutionService) {
        this.solutionService = solutionService;
    }

    @GetMapping
    public SaResult getAllSolutions() {
        return SaResult.data(solutionService.list());
    }

    @GetMapping("/{id}")
    public SaResult getSolutionById(@PathVariable Long id) {
        Solution solution = solutionService.getById(id);
        return solution == null ? SaResult.error("解决方案不存在").setCode(404) : SaResult.data(solution);
    }

    @PostMapping
    public SaResult createSolution(@Valid @RequestBody Solution solution) {
        return solutionService.save(solution) ? SaResult.ok("创建解决方案成功") : SaResult.error("创建解决方案失败");
    }

    @PutMapping("/{id}")
    public SaResult updateSolution(@PathVariable Long id, @Valid @RequestBody Solution solution) {
        solution.setId(id);
        return solutionService.saveOrUpdate(solution) ? SaResult.ok("更新解决方案成功") : SaResult.error("更新解决方案失败");
    }

    @DeleteMapping("/{id}")
    public SaResult deleteSolution(@PathVariable Long id) {
        return solutionService.removeById(id) ? SaResult.ok("删除解决方案成功") : SaResult.error("解决方案不存在或已删除").setCode(404);
    }
}

package org.codingtext.admin.controller;

import lombok.RequiredArgsConstructor;
import org.codingtext.admin.dto.problem.ProblemListResponse;
import org.codingtext.admin.dto.problem.ProblemRequest;
import org.codingtext.admin.service.ProblemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/codes")
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping
    public ResponseEntity<Page<ProblemListResponse>> findAllCodeProblems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(problemService.findAllCodeProblems(page, size));
    }
    @PostMapping
    public ResponseEntity<?> addNewProblem(@RequestBody ProblemRequest problemRequest) {
        return new ResponseEntity<>(problemService.createProblem(problemRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/{codeId}")
    public ResponseEntity<?> deleteProblem(@PathVariable Long codeId) {
        return ResponseEntity.ok(problemService.deleteProblem(codeId));
    }
}

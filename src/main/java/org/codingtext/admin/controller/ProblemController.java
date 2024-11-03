package org.codingtext.admin.controller;

import lombok.RequiredArgsConstructor;
import org.codingtext.admin.dto.problem.ProblemRequest;
import org.codingtext.admin.service.ProblemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/codes")
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping
    public ResponseEntity<?> findAllCodeProblems() {
        return ResponseEntity.ok(problemService.findAllCodeProblems());
    }

    @PostMapping
    public ResponseEntity<?> addNewProblem(@RequestBody ProblemRequest problemRequest) {
        return new ResponseEntity<>(problemService.createProblem(problemRequest), HttpStatus.CREATED);
    }
}

package com.example.student.controller;

import com.example.student.model.ClassRequest;
import com.example.student.model.ClassResponse;
import com.example.student.model.ClassSearchRequest;
import com.example.student.model.PagingResponse;
import com.example.student.service.ClassService;
import com.example.student.validation.constraint.ClassIdValid;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student-service/classes")
@Validated
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;

    @GetMapping
    public ResponseEntity<PagingResponse<ClassResponse>> getClassName(ClassSearchRequest request) {
        return ResponseEntity.ok(classService.search(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassResponse> getClassById(@PathVariable Long id) {
        return ResponseEntity.ok(classService.getClassById(id));
    }

    @PostMapping
    public ResponseEntity<ClassResponse> saveClass(@RequestBody @Validated ClassRequest request) {
        return ResponseEntity.ok(classService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassResponse> updateClass(@PathVariable @Valid @ClassIdValid Long id, @RequestBody @Validated ClassRequest request) {
        return ResponseEntity.ok(classService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable @Valid @ClassIdValid Long id) {
        classService.delete(id);
        return ResponseEntity.ok().build();
    }
}

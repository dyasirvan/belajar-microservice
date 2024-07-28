package com.example.student.controller;

import com.example.student.model.PagingResponse;
import com.example.student.model.StudentRequest;
import com.example.student.model.StudentResponse;
import com.example.student.model.StudentSearchRequest;
import com.example.student.service.StudentService;
import com.example.student.validation.constraint.StudentIdValid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student-service/students")
@Validated
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<PagingResponse<StudentResponse>> getStudentName(StudentSearchRequest request) {
        return ResponseEntity.ok(studentService.search(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.get(id));
    }

    @PostMapping
    public ResponseEntity<StudentResponse> saveStudent(@RequestBody @Validated StudentRequest request) {
        return ResponseEntity.ok(studentService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable @StudentIdValid Long id, @RequestBody @Validated StudentRequest request) {
        return ResponseEntity.ok(studentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable @StudentIdValid Long id) {
        studentService.delete(id);
        return ResponseEntity.ok().build();
    }
}

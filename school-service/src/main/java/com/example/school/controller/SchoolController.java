package com.example.school.controller;

import com.example.school.model.PagingResponse;
import com.example.school.model.SchoolRequest;
import com.example.school.model.SchoolResponse;
import com.example.school.model.SchoolSearchRequest;
import com.example.school.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/school-service/schools")
@RequiredArgsConstructor
@Validated
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping
    public ResponseEntity<PagingResponse<SchoolResponse>> getSchoolName(SchoolSearchRequest request) {
        return ResponseEntity.ok(schoolService.search(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolResponse> getSchoolById(@PathVariable Long id) {
        return ResponseEntity.ok(schoolService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SchoolResponse> saveSchool(@RequestBody @Validated SchoolRequest request) {
        return ResponseEntity.ok(schoolService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SchoolResponse> updateSchool(@PathVariable Long id, @RequestBody @Validated SchoolRequest request) {
        return ResponseEntity.ok(schoolService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchool(@PathVariable Long id) {
        schoolService.delete(id);
        return ResponseEntity.ok().build();
    }
}

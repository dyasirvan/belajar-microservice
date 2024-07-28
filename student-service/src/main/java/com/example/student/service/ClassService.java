package com.example.student.service;

import com.example.student.client.SchoolClient;
import com.example.student.entity.ClassEntity;
import com.example.student.entity.Student;
import com.example.student.model.*;
import com.example.student.repository.ClassRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassService {

    private final ClassRepository classRepository;
    private final SchoolClient schoolClient;

    public ClassResponse save(ClassRequest classRequest) {
        ClassEntity classEntity = new ClassEntity();
        classEntity.setName(classRequest.getName());
        classEntity.setSchoolId(classRequest.getSchoolId());
        classEntity.setStudents(new ArrayList<>());

        classEntity = classRepository.save(classEntity);
        return toClassResponse(classEntity);
    }

    public PagingResponse<ClassResponse> search(ClassSearchRequest classSearchRequest) {
        Specification<ClassEntity> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(classSearchRequest.getName())) {
                predicates.add(builder.like(root.get("name"), "%" + classSearchRequest.getName() + "%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(classSearchRequest.getPage(), classSearchRequest.getSize());
        Page<ClassEntity> classEntities = classRepository.findAll(specification, pageable);
        List<ClassResponse> classResponses = classEntities.stream().map(this::toClassResponse).collect(Collectors.toList());
        return PagingResponse.<ClassResponse>builder()
                .size(classResponses.size())
                .data(classResponses)
                .currentPage(classEntities.getNumber())
                .totalPage(classEntities.getTotalPages())
                .build();
    }

    public ClassResponse getClassById(Long classId) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new EntityNotFoundException("Class not found"));
        return toClassResponse(classEntity);
    }

    public ClassResponse update(Long classId, ClassRequest classRequest) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new EntityNotFoundException("Class not found"));
        classEntity.setName(classRequest.getName());
        classEntity.setSchoolId(classRequest.getSchoolId());

        classEntity = classRepository.save(classEntity);
        return toClassResponse(classEntity);
    }

    public void delete(Long classId) {
        classRepository.deleteById(classId);
    }

    private ClassResponse toClassResponse(ClassEntity classEntity) {
        ClassResponse classResponse = new ClassResponse();
        classResponse.setId(classEntity.getId());
        classResponse.setName(classEntity.getName());

        // get school by id
        SchoolResponse schoolResponse = schoolClient.getSchoolById(classEntity.getSchoolId()).block();
        classResponse.setSchool(schoolResponse);

        // mapping student
        List<Student> students = classEntity.getStudents();
        List<StudentResponse> studentResponses = students.stream()
                .map(student -> StudentResponse.builder()
                        .id(student.getId())
                        .name(student.getName())
                        .email(student.getEmail())
                        .phone(student.getPhone())
                        .build())
                .collect(Collectors.toList());
        classResponse.setStudents(studentResponses);

        return classResponse;
    }
}

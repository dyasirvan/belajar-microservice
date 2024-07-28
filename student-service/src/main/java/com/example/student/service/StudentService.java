package com.example.student.service;

import com.example.student.entity.ClassEntity;
import com.example.student.entity.Student;
import com.example.student.model.*;
import com.example.student.repository.ClassRepository;
import com.example.student.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;

    public StudentResponse save(StudentRequest studentRequest) {
        Student student = new Student();
        this.populateStudentFields(student, studentRequest);
        student = studentRepository.save(student);
        return toStudentResponse(student);
    }

    public PagingResponse<StudentResponse> search(StudentSearchRequest studentSearchRequest) {
        Specification<Student> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (studentSearchRequest.getName() != null) {
                predicates.add(builder.like(root.get("name"), "%" + studentSearchRequest.getName() + "%"));
            }

            if (studentSearchRequest.getEmail() != null) {
                predicates.add(builder.like(root.get("email"), "%" + studentSearchRequest.getEmail() + "%"));
            }

            if (studentSearchRequest.getPhone() != null) {
                predicates.add(builder.like(root.get("phone"), "%" + studentSearchRequest.getPhone() + "%"));
            }

            if (studentSearchRequest.getAddress() != null) {
                predicates.add(builder.like(root.get("address"), "%" + studentSearchRequest.getAddress() + "%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(studentSearchRequest.getPage(), studentSearchRequest.getSize());
        Page<Student> addresses = studentRepository.findAll(specification, pageable);
        List<StudentResponse> contactResponses = addresses.getContent().stream()
                .map(this::toStudentResponse)
                .toList();

        PageImpl<StudentResponse> addressResponsePage = new PageImpl<>(contactResponses, pageable, addresses.getTotalElements());
        return PagingResponse.<StudentResponse>builder()
                .data(addressResponsePage.getContent())
                .size(addressResponsePage.getSize())
                .currentPage(addressResponsePage.getNumber())
                .totalPage(addressResponsePage.getTotalPages())
                .build();
    }

    public StudentResponse get(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        return toStudentResponse(student);
    }

    public StudentResponse update(Long id, StudentRequest studentRequest) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        this.populateStudentFields(student, studentRequest);
        student = studentRepository.save(student);
        return toStudentResponse(student);
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    private void populateStudentFields(Student student, StudentRequest studentRequest) {
        student.setName(studentRequest.getName());
        student.setEmail(studentRequest.getEmail());
        student.setPhone(studentRequest.getPhone());
        student.setAddress(studentRequest.getAddress());

        // set class entity
        ClassEntity classEntity = classRepository.findById(studentRequest.getClassId())
                .orElseThrow(() -> new EntityNotFoundException("Class not found"));
        student.setClassEntity(classEntity);
    }

    private StudentResponse toStudentResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .name(student.getName())
                .email(student.getEmail())
                .phone(student.getPhone())
                .address(student.getAddress())
                .classResponse(ClassResponse.builder()
                        .id(student.getClassEntity().getId())
                        .name(student.getClassEntity().getName())
                        .build())
                .build();
    }

}

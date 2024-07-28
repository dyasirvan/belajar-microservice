package com.example.student.validation;

import com.example.student.repository.StudentRepository;
import com.example.student.validation.constraint.UniqueStudentName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueStudentNameValidator implements ConstraintValidator<UniqueStudentName, String> {

    private final StudentRepository studentRepository;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return studentRepository.findByName(name).isEmpty();
    }
}
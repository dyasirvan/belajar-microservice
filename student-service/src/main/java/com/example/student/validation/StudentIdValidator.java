package com.example.student.validation;

import com.example.student.repository.StudentRepository;
import com.example.student.validation.constraint.StudentIdValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudentIdValidator implements ConstraintValidator<StudentIdValid, Long> {

    private final StudentRepository studentRepository;

    @Override
    public boolean isValid(Long studentId, ConstraintValidatorContext context) {
        return studentRepository.existsById(studentId);
    }
}
package com.example.student.validation;

import com.example.student.repository.ClassRepository;
import com.example.student.validation.constraint.ClassIdValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClassIdValidator implements ConstraintValidator<ClassIdValid, Long> {

    private final ClassRepository classRepository;

    @Override
    public boolean isValid(Long classId, ConstraintValidatorContext context) {
        return classRepository.existsById(classId);
    }
}
package com.example.student.validation;

import com.example.student.repository.ClassRepository;
import com.example.student.validation.constraint.UniqueClassName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueClassNameValidator implements ConstraintValidator<UniqueClassName, String> {

    private final ClassRepository classRepository;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return classRepository.findByName(name).isEmpty();
    }
}
package com.example.student.validation;

import com.example.student.repository.StudentRepository;
import com.example.student.validation.constraint.UniqueEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final StudentRepository studentRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return studentRepository.findByEmail(email).isEmpty();
    }
}
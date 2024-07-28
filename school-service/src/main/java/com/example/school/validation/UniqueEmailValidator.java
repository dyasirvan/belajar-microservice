package com.example.school.validation;

import com.example.school.repository.SchoolRepository;
import com.example.school.validation.constraint.UniqueEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final SchoolRepository schoolRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return schoolRepository.findByEmail(email).isEmpty();
    }
}
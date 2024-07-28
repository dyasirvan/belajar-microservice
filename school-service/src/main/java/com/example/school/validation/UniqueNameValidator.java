package com.example.school.validation;

import com.example.school.repository.SchoolRepository;
import com.example.school.validation.constraint.UniqueName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueNameValidator implements ConstraintValidator<UniqueName, String> {

    private final SchoolRepository schoolRepository;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return schoolRepository.findByName(name).isEmpty();
    }
}
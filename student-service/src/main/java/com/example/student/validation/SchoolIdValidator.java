package com.example.student.validation;

import com.example.student.client.SchoolClient;
import com.example.student.validation.constraint.SchoolIdValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SchoolIdValidator implements ConstraintValidator<SchoolIdValid, Long> {

    private final SchoolClient schoolClient;

    @Override
    public boolean isValid(Long schoolId, ConstraintValidatorContext context) {
        return schoolClient.getSchoolById(schoolId).block() != null;
    }
}
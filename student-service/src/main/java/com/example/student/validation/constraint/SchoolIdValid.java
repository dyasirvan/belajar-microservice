package com.example.student.validation.constraint;

import com.example.student.validation.SchoolIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SchoolIdValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface SchoolIdValid {
    String message() default "School Id is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
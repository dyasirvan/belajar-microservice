package com.example.student.validation.constraint;

import com.example.student.validation.StudentIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Constraint(validatedBy = StudentIdValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface StudentIdValid {
    String message() default "Student Id is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
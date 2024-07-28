package com.example.student.validation.constraint;

import com.example.student.validation.ClassIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Constraint(validatedBy = ClassIdValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassIdValid {
    String message() default "Class Id is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
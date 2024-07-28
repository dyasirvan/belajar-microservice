package com.example.student.validation.constraint;

import com.example.student.validation.UniqueStudentNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueStudentNameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueStudentName {
    String message() default "Student Name is already used";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
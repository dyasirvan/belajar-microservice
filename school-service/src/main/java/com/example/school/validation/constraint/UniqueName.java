package com.example.school.validation.constraint;

import com.example.school.validation.UniqueNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueNameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueName {
    String message() default "School Name is already used";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
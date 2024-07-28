package com.example.student.model;

import com.example.student.validation.constraint.SchoolIdValid;
import com.example.student.validation.constraint.UniqueClassName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassRequest {

    @NotNull
    @UniqueClassName
    private String name;

    @NotNull
    @SchoolIdValid
    private Long schoolId;

}

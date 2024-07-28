package com.example.school.service;

import com.example.school.entity.School;
import com.example.school.model.PagingResponse;
import com.example.school.model.SchoolRequest;
import com.example.school.model.SchoolResponse;
import com.example.school.model.SchoolSearchRequest;
import com.example.school.repository.SchoolRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SchoolService {

    private final SchoolRepository schoolRepository;

    public SchoolResponse save(SchoolRequest schoolRequest) {
        School school = new School();
        school.setName(schoolRequest.getName());
        school.setEmail(schoolRequest.getEmail());
        school.setPhone(schoolRequest.getPhone());
        school.setAddress(schoolRequest.getAddress());

        school = schoolRepository.save(school);
        return toSchoolResponse(school);
    }

    public PagingResponse<SchoolResponse> search(SchoolSearchRequest schoolSearchRequest) {
        Specification<School> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(schoolSearchRequest.getName())) {
                predicates.add(builder.like(root.get("name"), "%" + schoolSearchRequest.getName() + "%"));
            }

            if (Objects.nonNull(schoolSearchRequest.getEmail())) {
                predicates.add(builder.like(root.get("email"), "%" + schoolSearchRequest.getEmail() + "%"));
            }

            if (Objects.nonNull(schoolSearchRequest.getPhone())) {
                predicates.add(builder.like(root.get("phone"), "%" + schoolSearchRequest.getPhone() + "%"));
            }

            if (Objects.nonNull(schoolSearchRequest.getAddress())) {
                predicates.add(builder.like(root.get("address"), "%" + schoolSearchRequest.getAddress() + "%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(schoolSearchRequest.getPage(), schoolSearchRequest.getSize());
        Page<School> schools = schoolRepository.findAll(specification, pageable);
        List<SchoolResponse> contactResponses = schools.getContent().stream()
                .map(this::toSchoolResponse)
                .toList();
        PageImpl<SchoolResponse> addressResponsePage = new PageImpl<>(contactResponses, pageable, schools.getTotalElements());
        return PagingResponse.<SchoolResponse>builder()
                .data(addressResponsePage.getContent())
                .currentPage(addressResponsePage.getNumber())
                .size(addressResponsePage.getSize())
                .totalPage(addressResponsePage.getTotalPages())
                .build();

    }

    public SchoolResponse findById(Long id) {
        School school = schoolRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("School not found"));
        return toSchoolResponse(school);
    }

    public SchoolResponse update(Long id, SchoolRequest schoolRequest) {
        School school = schoolRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("School not found"));
        school.setName(schoolRequest.getName());
        school.setEmail(schoolRequest.getEmail());
        school.setPhone(schoolRequest.getPhone());
        school.setAddress(schoolRequest.getAddress());

        School schoolSaved = schoolRepository.save(school);
        return toSchoolResponse(schoolSaved);
    }

    public void delete(Long id) {
        School school = schoolRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("School not found"));
        schoolRepository.delete(school);
    }


    private SchoolResponse toSchoolResponse(School school) {
        SchoolResponse schoolResponse = new SchoolResponse();
        schoolResponse.setId(school.getId());
        schoolResponse.setName(school.getName());
        schoolResponse.setEmail(school.getEmail());
        schoolResponse.setPhone(school.getPhone());
        schoolResponse.setAddress(school.getAddress());
        schoolResponse.setCreatedAt(school.getCreatedAt());
        schoolResponse.setUpdatedAt(school.getUpdatedAt());

        return schoolResponse;
    }

}

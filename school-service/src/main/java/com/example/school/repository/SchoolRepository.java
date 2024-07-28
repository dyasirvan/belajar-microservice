package com.example.school.repository;

import com.example.school.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long>, JpaSpecificationExecutor<School> {

    @Query("SELECT s FROM schools s WHERE s.name = :name")
    Optional<School> findByName(String name);

    @Query("SELECT s FROM schools s WHERE s.email = :email")
    Optional<School> findByEmail(String email);
}

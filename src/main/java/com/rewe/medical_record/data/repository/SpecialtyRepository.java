package com.rewe.medical_record.data.repository;

import com.rewe.medical_record.data.entity.SpecialtyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecialtyRepository extends JpaRepository<SpecialtyEntity, Long> {
    Optional<SpecialtyEntity> findByName(String name);
}

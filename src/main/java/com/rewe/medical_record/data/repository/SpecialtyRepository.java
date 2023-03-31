package com.rewe.medical_record.data.repository;

import com.rewe.medical_record.data.entity.SpecialtyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpecialtyRepository extends JpaRepository<SpecialtyEntity, Long> {
    List<SpecialtyEntity> findAllByDeletedFalse();
    Optional<SpecialtyEntity> findByIdAndDeletedFalse(Long id);
    Optional<SpecialtyEntity> findByNameAndDeletedFalse(String name);
}

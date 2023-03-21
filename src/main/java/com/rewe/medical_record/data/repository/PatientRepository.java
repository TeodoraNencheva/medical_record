package com.rewe.medical_record.data.repository;

import com.rewe.medical_record.data.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<PatientEntity, Long> {
}

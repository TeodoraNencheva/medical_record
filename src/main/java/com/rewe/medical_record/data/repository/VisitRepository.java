package com.rewe.medical_record.data.repository;

import com.rewe.medical_record.data.entity.VisitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<VisitEntity, Long> {
}

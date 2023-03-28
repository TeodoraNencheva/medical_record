package com.rewe.medical_record.data.repository;

import com.rewe.medical_record.data.entity.FeeHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeeHistoryRepository extends JpaRepository<FeeHistoryEntity, Long> {
}

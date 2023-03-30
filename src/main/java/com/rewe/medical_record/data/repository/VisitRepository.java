package com.rewe.medical_record.data.repository;

import com.rewe.medical_record.data.entity.VisitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<VisitEntity, Long> {
    Optional<VisitEntity> findByIdAndDeletedFalse(Long id);

    List<VisitEntity> findAllByDeletedFalse();

    @Query("SELECT SUM(v.fee.price) FROM VisitEntity v")
    BigDecimal getAllVisitsIncome();

    @Query("select sum(v.fee.price) from VisitEntity v " +
            "where v.doctor.id=:id")
    BigDecimal getVisitsIncomeByDoctorId(Long id);

    long countAllByPatientId(Long id);

    @Query("select count(v) from VisitEntity v where " +
            ":id in (select d.id from DiagnosisEntity d " +
            "where d member of v.diagnoses)")
    long countAllByContainingDiagnosisId(Long id);
}

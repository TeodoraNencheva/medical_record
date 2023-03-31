package com.rewe.medical_record.data.repository;

import com.rewe.medical_record.data.entity.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<DoctorEntity, Long> {
    List<DoctorEntity> findAllByDeletedFalse();

    Optional<DoctorEntity> findByIdAndDeletedFalse(Long id);

    @Query("select count(d) from DoctorEntity d " +
            "where (select COALESCE(sum(v.fee.price), 0) from VisitEntity v where v.doctor=d) > :income")
    long countByIncomeGreaterThan(BigDecimal income);

    @Query("select COALESCE(sum(v.fee.price), 0) from VisitEntity v " +
            "where v.doctor=:doctorEntity and v.paidByPatient=false")
    BigDecimal getIncomeByDoctorByInsuredPatients(DoctorEntity doctorEntity);
}

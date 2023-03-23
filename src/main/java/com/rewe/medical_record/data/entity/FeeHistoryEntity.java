package com.rewe.medical_record.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fee_history")
@NoArgsConstructor
public class FeeHistoryEntity extends BaseEntity {
    @CreationTimestamp
    @NotNull
    private LocalDate date;
    @NotNull
    @Positive
    private BigDecimal value;
}

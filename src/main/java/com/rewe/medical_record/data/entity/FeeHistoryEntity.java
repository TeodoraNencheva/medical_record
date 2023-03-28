package com.rewe.medical_record.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fee_history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FeeHistoryEntity extends BaseEntity {
    @NotNull
    private LocalDate effectiveFrom = LocalDate.now();
    @NotNull
    @Positive
    private BigDecimal value;
}

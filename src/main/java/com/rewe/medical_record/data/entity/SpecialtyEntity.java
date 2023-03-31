package com.rewe.medical_record.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "specialty")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialtyEntity extends BaseEntity {
    @Column(nullable = false, unique = true)
    @Size(min = 3, message = "Specialty name should be 3 or more characters")
    private String name;
    private String description;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted;
}

package com.rewe.medical_record.service;

import com.rewe.medical_record.data.dto.visit.AddVisitDto;
import com.rewe.medical_record.data.dto.visit.UpdateVisitDto;
import com.rewe.medical_record.data.dto.visit.VisitInfoDto;
import com.rewe.medical_record.data.entity.*;
import com.rewe.medical_record.data.repository.VisitRepository;
import com.rewe.medical_record.exceptions.VisitNotFoundException;
import com.rewe.medical_record.mapper.VisitMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitServiceTest {
    @Mock
    private VisitRepository visitRepository;
    @Mock
    private VisitMapper visitMapper;
    @InjectMocks
    private VisitService visitService;
    private VisitEntity firstVisit, secondVisit;

    @BeforeEach
    void setUp() {
        firstVisit = new VisitEntity();
        secondVisit = new VisitEntity();
    }

    @Test
    @DisplayName("Test get all non-deleted visits returns non-empty list")
    void getAllNonDeletedVisitsReturnsNonEmptyList() {
        when(visitRepository.findAllByDeletedFalse()).thenReturn(List.of(firstVisit, secondVisit));
        VisitInfoDto visitInfoDto = new VisitInfoDto();
        when(visitMapper.visitEntityToVisitInfoDto(any(VisitEntity.class))).thenReturn(visitInfoDto);
        assertIterableEquals(List.of(visitInfoDto, visitInfoDto), visitService.getAllVisits());
    }

    @Test
    @DisplayName("Test get all non-deleted visits returns empty list")
    void getAllNonDeletedVisitsReturnsEmptyList() {
        when(visitRepository.findAllByDeletedFalse()).thenReturn(List.of());
        assertIterableEquals(List.of(), visitService.getAllVisits());
    }

    @Test
    @DisplayName("Test get visit info returns result")
    void getVisitInfoReturnsResult() {
        when(visitRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(firstVisit));
        VisitInfoDto visitInfoDto = new VisitInfoDto();
        when(visitMapper.visitEntityToVisitInfoDto(any(VisitEntity.class))).thenReturn(visitInfoDto);
        assertEquals(visitInfoDto, visitService.getVisitInfo(1L));
    }

    @Test
    @DisplayName("Test get visit info throws")
    void getVisitInfoThrows() {
        when(visitRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());
        assertThrows(VisitNotFoundException.class, () -> visitService.getVisitInfo(1L));
    }

    @Test
    @DisplayName("Test add visit")
    void testGetAddVisit() {
        AddVisitDto addVisitDto = new AddVisitDto();
        when(visitMapper.addVisitDtoToVisitEntity(addVisitDto)).thenReturn(secondVisit);
        when(visitRepository.save(secondVisit)).thenReturn(secondVisit);
        VisitInfoDto visitInfoDto = new VisitInfoDto();
        when(visitMapper.visitEntityToVisitInfoDto(secondVisit)).thenReturn(visitInfoDto);
        assertEquals(visitInfoDto, visitService.addVisit(addVisitDto));
    }

    @Test
    @DisplayName("Test update visit with valid id")
    void testUpdateVisitWithValidId() {
        when(visitRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(firstVisit));

        ReflectionTestUtils.setField(secondVisit, "doctor", mock(DoctorEntity.class));
        ReflectionTestUtils.setField(secondVisit, "patient", mock(PatientEntity.class));
        ReflectionTestUtils.setField(secondVisit, "diagnoses", Set.of(mock(DiagnosisEntity.class)));
        ReflectionTestUtils.setField(secondVisit, "paidByPatient", true);

        UpdateVisitDto updateVisitDto = new UpdateVisitDto();
        ReflectionTestUtils.setField(updateVisitDto, "id", 1L);
        when(visitMapper.updateVisitDtoToVisitEntity(updateVisitDto)).thenReturn(secondVisit);
        when(visitRepository.save(firstVisit)).thenReturn(firstVisit);

        VisitInfoDto visitInfoDto = new VisitInfoDto();
        when(visitMapper.visitEntityToVisitInfoDto(firstVisit)).thenReturn(visitInfoDto);

        assertEquals(visitInfoDto, visitService.updateVisit(updateVisitDto));
        assertEquals(firstVisit.getDoctor(), secondVisit.getDoctor());
        assertEquals(firstVisit.getPatient(), secondVisit.getPatient());
        assertEquals(firstVisit.getDiagnoses(), secondVisit.getDiagnoses());
        assertEquals(firstVisit.isPaidByPatient(), secondVisit.isPaidByPatient());
    }

    @Test
    @DisplayName("Test update visit with invalid id")
    void testUpdateVisitWithInvalidId() {
        when(visitRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());
        UpdateVisitDto updateVisitDto = new UpdateVisitDto();
        ReflectionTestUtils.setField(updateVisitDto, "id", 1L);
        assertThrows(VisitNotFoundException.class, () -> visitService.updateVisit(updateVisitDto));
    }

    @Test
    @DisplayName("Test delete visit with valid id")
    void testDeleteVisitWithValidId() {
        when(visitRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(firstVisit));
        visitService.deleteVisit(1L);
        assertTrue(firstVisit.isDeleted());
        verify(visitRepository).save(firstVisit);
    }

    @Test
    @DisplayName("Test delete visit with invalid id")
    void testDeleteVisitWithInvalidId() {
        when(visitRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());
        assertThrows(VisitNotFoundException.class, () -> visitService.deleteVisit(1L));
    }

    @Test
    @DisplayName("Test getTotalVisitsIncome()")
    void testGetTotalVisitsIncome() {
        when(visitRepository.getAllVisitsIncome()).thenReturn(new BigDecimal("115.93"));
        assertEquals(new BigDecimal("115.93"), visitService.getTotalVisitsIncome());
    }

    @Test
    @DisplayName("Test getVisitsIncomeByDoctorId")
    void testGetVisitsIncomeByDoctorId() {
        when(visitRepository.getVisitsIncomeByDoctorId(anyLong())).thenReturn(new BigDecimal("98.43"));
        assertEquals(new BigDecimal("98.43"), visitService.getVisitsIncomeByDoctorId(5L));
    }

    @Test
    @DisplayName("Test countAllByPatientId")
    void testCountAllByPatientId() {
        when(visitRepository.countAllByPatientId(anyLong())).thenReturn(54L);
        assertEquals(54L, visitService.countAllByPatientId(4L));
    }

    @Test
    @DisplayName("Test countAllByContainingDiagnosisId")
    void testCountAllByContainingDiagnosisId() {
        when(visitRepository.countAllByContainingDiagnosisId(anyLong())).thenReturn(32L);
        assertEquals(32L, visitService.countAllByContainingDiagnosisId(6L));
    }
}
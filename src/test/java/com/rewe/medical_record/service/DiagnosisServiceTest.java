package com.rewe.medical_record.service;

import com.rewe.medical_record.data.dto.diagnosis.AddDiagnosisDto;
import com.rewe.medical_record.data.dto.diagnosis.DiagnosisInfoDto;
import com.rewe.medical_record.data.dto.diagnosis.UpdateDiagnosisDto;
import com.rewe.medical_record.data.entity.DiagnosisEntity;
import com.rewe.medical_record.data.repository.DiagnosisRepository;
import com.rewe.medical_record.exceptions.DiagnosisNotFoundException;
import com.rewe.medical_record.mapper.DiagnosisMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiagnosisServiceTest {
    @Mock
    private DiagnosisRepository diagnosisRepository;
    @Mock
    private DiagnosisMapper diagnosisMapper;
    @InjectMocks
    private DiagnosisService diagnosisService;
    private DiagnosisEntity first, second;

    @BeforeEach
    void setUp() {
        first = new DiagnosisEntity("flu", "flu", false);
        ReflectionTestUtils.setField(first, "id", 1L);
        second = new DiagnosisEntity("asthma", "asthma", false);
        ReflectionTestUtils.setField(second, "id", 2L);
    }

    @Test
    @DisplayName("Test get all diagnoses")
    void getAllDiagnosesTest() {
        when(diagnosisRepository.findAllByDeletedFalse()).thenReturn(List.of(first, second));

        DiagnosisInfoDto infoDto1 = new DiagnosisInfoDto(1L, "flu", "flu");
        DiagnosisInfoDto infoDto2 = new DiagnosisInfoDto(2L, "asthma", "asthma");
        when(diagnosisMapper.diagnosisEntityToDiagnosisInfoDto(first)).thenReturn(infoDto1);
        when(diagnosisMapper.diagnosisEntityToDiagnosisInfoDto(second)).thenReturn(infoDto2);

        assertIterableEquals(List.of(infoDto1, infoDto2), diagnosisService.getAllDiagnoses());
    }

    @Test
    @DisplayName("Test get diagnosis with valid id")
    void testGetDiagnosisWithValidId() {
        when(diagnosisRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(second));
        DiagnosisInfoDto infoDto = new DiagnosisInfoDto(2L, "asthma", "asthma");
        when(diagnosisMapper.diagnosisEntityToDiagnosisInfoDto(second)).thenReturn(infoDto);

        assertEquals(infoDto, diagnosisService.getDiagnosis(2L));
    }

    @Test
    @DisplayName("Test get diagnosis with invalid id")
    void testGetDiagnosisWithInvalidId() {
        when(diagnosisRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());
        assertThrows(DiagnosisNotFoundException.class, () -> diagnosisService.getDiagnosis(1L));
    }

    @Test
    @DisplayName("Test create diagnosis")
    void testCreateDiagnosis() {
        AddDiagnosisDto addDto = new AddDiagnosisDto("flu", "flu");
        when(diagnosisMapper.addDiagnosisDtoToDiagnosisEntity(any(AddDiagnosisDto.class))).thenReturn(first);
        when(diagnosisRepository.save(first)).thenReturn(first);
        DiagnosisInfoDto infoDto = new DiagnosisInfoDto(1L, "flu", "flu");
        when(diagnosisMapper.diagnosisEntityToDiagnosisInfoDto(first)).thenReturn(infoDto);
        assertEquals(infoDto, diagnosisService.createDiagnosis(addDto));
    }

    @Test
    @DisplayName("Test update diagnosis with valid id")
    void testUpdateDiagnosisWithValidId() {
        UpdateDiagnosisDto updateDto = new UpdateDiagnosisDto(1L, "flu", "flu");
        when(diagnosisRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(second));
        when(diagnosisMapper.updateDiagnosisDtoToDiagnosisEntity(any(UpdateDiagnosisDto.class))).thenReturn(first);
        when(diagnosisRepository.save(any(DiagnosisEntity.class))).thenReturn(second);
        DiagnosisInfoDto infoDto = new DiagnosisInfoDto(1L, "flu", "flu");
        when(diagnosisMapper.diagnosisEntityToDiagnosisInfoDto(any(DiagnosisEntity.class))).thenReturn(infoDto);

        assertEquals(infoDto, diagnosisService.updateDiagnosis(updateDto));
        assertEquals(first.getName(), second.getName());
        assertEquals(first.getDescription(), second.getDescription());
    }

    @Test
    @DisplayName("Test update diagnosis with invalid id")
    void testUpdateDiagnosisWithInvalidId() {
        UpdateDiagnosisDto updateDto = new UpdateDiagnosisDto(1L, "flu", "flu");
        when(diagnosisRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());
        assertThrows(DiagnosisNotFoundException.class, () -> diagnosisService.updateDiagnosis(updateDto));
    }

    @Test
    @DisplayName("Test delete diagnosis with valid id")
    void testDeleteDiagnosisWithValidId() {
        when(diagnosisRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(second));
        diagnosisService.deleteDiagnosis(1L);
        assertTrue(second.isDeleted());
        verify(diagnosisRepository).save(second);
    }

    @Test
    @DisplayName("Test delete diagnosis with invalid id")
    void testDeleteDiagnosisWithInvalidId() {
        when(diagnosisRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());
        assertThrows(DiagnosisNotFoundException.class, () -> diagnosisService.deleteDiagnosis(1L));
        verify(diagnosisRepository, never()).save(any(DiagnosisEntity.class));
    }
}
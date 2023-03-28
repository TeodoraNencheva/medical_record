package com.rewe.medical_record.service;

import com.rewe.medical_record.data.dto.patient.AddPatientDto;
import com.rewe.medical_record.data.dto.patient.PatientInfoDTO;
import com.rewe.medical_record.data.dto.patient.UpdatePatientDto;
import com.rewe.medical_record.data.entity.GeneralPractitionerEntity;
import com.rewe.medical_record.data.entity.PatientEntity;
import com.rewe.medical_record.data.entity.SpecialtyEntity;
import com.rewe.medical_record.data.repository.PatientRepository;
import com.rewe.medical_record.exceptions.PatientNotFoundException;
import com.rewe.medical_record.mapper.PatientMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private PatientMapper patientMapper;
    @InjectMocks
    private PatientService patientService;
    private PatientEntity firstPatient, secondPatient;

    @BeforeEach
    void setUp() {
        GeneralPractitionerEntity gp = new GeneralPractitionerEntity();
        ReflectionTestUtils.setField(gp, "id", 1L);
        ReflectionTestUtils.setField(gp, "name", "Ivan Ivanov");
        ReflectionTestUtils.setField(gp, "birthDate", LocalDate.of(1980, 2, 2));
        SpecialtyEntity specialty = new SpecialtyEntity();
        ReflectionTestUtils.setField(specialty, "id", 1L);
        ReflectionTestUtils.setField(specialty, "name", "Cardiologist");
        ReflectionTestUtils.setField(gp, "specialties", Set.of(specialty));

        firstPatient = new PatientEntity("Petar Petrov", gp, true, false);
        ReflectionTestUtils.setField(firstPatient, "id", 1L);
        secondPatient = new PatientEntity("Hristo Hristov", gp, false, false);
        ReflectionTestUtils.setField(secondPatient, "id", 2L);
    }

    @Test
    @DisplayName("Test get all patients with non empty list")
    void getAllPatientsTestWithNonEmptyList() {
        List<PatientEntity> list = List.of(firstPatient, secondPatient);
        when(patientRepository.findAllByDeletedFalse()).thenReturn(list);

        PatientInfoDTO firstDto = new PatientInfoDTO(1L, "Petar Petrov", 1L, true);
        PatientInfoDTO secondDto = new PatientInfoDTO(2L, "Hristo Hristov", 1L, false);
        when(patientMapper.patientEntityToPatientInfoDto(firstPatient)).thenReturn(firstDto);
        when(patientMapper.patientEntityToPatientInfoDto(secondPatient)).thenReturn(secondDto);

        assertIterableEquals(List.of(firstDto, secondDto), patientService.getAllPatients());
    }

    @Test
    @DisplayName("Test get all patients with empty list")
    void getAllPatientsTestWithEmptyList() {
        when(patientRepository.findAllByDeletedFalse()).thenReturn(List.of());
        assertIterableEquals(List.of(), patientService.getAllPatients());
    }

    @Test
    @DisplayName("Test get patient info of non-deleted patient")
    void getNonDeletedPatientInfoTest() {
        when(patientRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(firstPatient));
        PatientInfoDTO infoDto = new PatientInfoDTO(1L, "Petar Petrov", 1L, true);
        when(patientMapper.patientEntityToPatientInfoDto(firstPatient)).thenReturn(infoDto);

        assertEquals(infoDto, patientService.getPatientInfo(1L));
    }

    @Test
    @DisplayName("Test get patient info of deleted or non-present patient")
    void getDeletedPatientInfoTest() {
        when(patientRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.empty());
        assertThrows(PatientNotFoundException.class, () -> patientService.getPatientInfo(1L));
    }

    @Test
    @DisplayName("Test add patient")
    void addPatientTest() {
        AddPatientDto patientDto = new AddPatientDto("Petar Petrov", 1L, true);
        when(patientMapper.addPatientDtoToPatientEntity(patientDto)).thenReturn(firstPatient);
        when(patientRepository.save(firstPatient)).thenReturn(firstPatient);

        PatientInfoDTO infoDto = new PatientInfoDTO(1L, "Petar Petrov", 1L, true);
        when(patientMapper.patientEntityToPatientInfoDto(firstPatient)).thenReturn(infoDto);

        assertEquals(infoDto, patientService.addPatient(patientDto));
    }

    @Test
    @DisplayName("Test update deleted or non-present patient")
    void updateDeletedPatientTest() {
        UpdatePatientDto patientDto = new UpdatePatientDto(1L, "Petar Petrov", 1L, true);
        when(patientRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> patientService.updatePatient(patientDto));
    }

    @Test
    @DisplayName("Test update non-deleted patient")
    void updateNonDeletedPatientTest() {
        UpdatePatientDto patientDto = new UpdatePatientDto(1L, "Petar Petrov", 1L, true);
        when(patientRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(firstPatient));
        when(patientMapper.updatePatientDtoToPatientEntity(patientDto)).thenReturn(firstPatient);

        PatientInfoDTO infoDto = new PatientInfoDTO(1L, "Petar Petrov", 1L, true);
        when(patientMapper.patientEntityToPatientInfoDto(firstPatient)).thenReturn(infoDto);
        when(patientRepository.save(firstPatient)).thenReturn(firstPatient);

        assertEquals(infoDto, patientService.updatePatient(patientDto));
    }

    @Test
    @DisplayName("Test delete deleted or non-present patient")
    void deleteDeletedPatientTest() {
        when(patientRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.empty());
        assertThrows(PatientNotFoundException.class, () -> patientService.deletePatient(1L));
    }

    @Test
    @DisplayName("Test delete non-deleted patient")
    void deleteNonDeletedPatientTest() {
        when(patientRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(firstPatient));
        patientService.deletePatient(1L);
        assertTrue(firstPatient.isDeleted());
        verify(patientRepository).save(firstPatient);
    }
}
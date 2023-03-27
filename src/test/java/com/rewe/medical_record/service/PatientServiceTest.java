package com.rewe.medical_record.service;

import com.rewe.medical_record.data.dto.patient.AddPatientDto;
import com.rewe.medical_record.data.dto.patient.PatientInfoDTO;
import com.rewe.medical_record.data.entity.GeneralPractitionerEntity;
import com.rewe.medical_record.data.entity.PatientEntity;
import com.rewe.medical_record.data.repository.PatientRepository;
import com.rewe.medical_record.enums.Specialty;
import com.rewe.medical_record.mapper.PatientMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private PatientMapper patientMapper;
    @InjectMocks
    private PatientService patientService;
    private PatientEntity patient;
    private PatientInfoDTO patientInfoDTO;
    private AddPatientDto addPatientDto;

    @BeforeEach
    void setUp() {
        GeneralPractitionerEntity gp = new GeneralPractitionerEntity();
        ReflectionTestUtils.setField(gp, "id", 1L);
        ReflectionTestUtils.setField(gp, "name", "Ivan Ivanov");
        ReflectionTestUtils.setField(gp, "birthDate", LocalDate.of(1980, 2, 2));
        ReflectionTestUtils.setField(gp, "specialties", Set.of(Specialty.CARDIOLOGIST));
        patient = new PatientEntity("Petar Petrov", gp, true);
        patientInfoDTO = new PatientInfoDTO(1L, "Petar Petrov", 1L, true);
        addPatientDto = new AddPatientDto("Petar Petrov", 1L, true);
    }

    @Test
    @DisplayName("Test get all patients")
    void getAllPatientsTest() {
        List<PatientEntity> list = List.of(patient);
        when(patientRepository.findAll()).thenReturn(list);
        when(patientMapper.patientEntityToPatientInfoDto(patient)).thenReturn(patientInfoDTO);
        assertIterableEquals(List.of(patientInfoDTO), patientService.getAllPatients());
    }

    @Test
    @DisplayName("Test get patient info test")
    void getPatientInfoTest() {
        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));
        when(patientMapper.patientEntityToPatientInfoDto(patient)).thenReturn(patientInfoDTO);
        assertEquals(patientInfoDTO, patientService.getPatientInfo(1L));
    }

    @Test
    @DisplayName("Test add patient")
    void addPatientTest() {
        when(patientMapper.addPatientDtoToPatientEntity(addPatientDto)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(patient);
        assertEquals(patientInfoDTO, patientService.addPatient(addPatientDto));
    }
}
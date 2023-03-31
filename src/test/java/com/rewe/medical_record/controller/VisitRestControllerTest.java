package com.rewe.medical_record.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rewe.medical_record.data.dto.visit.AddVisitDto;
import com.rewe.medical_record.data.dto.visit.UpdateVisitDto;
import com.rewe.medical_record.data.dto.visit.VisitInfoDto;
import com.rewe.medical_record.data.entity.DiagnosisEntity;
import com.rewe.medical_record.data.entity.DoctorEntity;
import com.rewe.medical_record.data.entity.PatientEntity;
import com.rewe.medical_record.data.entity.VisitEntity;
import com.rewe.medical_record.data.repository.DiagnosisRepository;
import com.rewe.medical_record.data.repository.DoctorRepository;
import com.rewe.medical_record.data.repository.PatientRepository;
import com.rewe.medical_record.data.repository.VisitRepository;
import com.rewe.medical_record.exceptions.DiagnosisNotFoundException;
import com.rewe.medical_record.exceptions.DoctorNotFoundException;
import com.rewe.medical_record.exceptions.PatientNotFoundException;
import com.rewe.medical_record.service.VisitService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VisitRestController.class)
@AutoConfigureMockMvc
class VisitRestControllerTest {
    @MockBean
    private VisitService visitService;
    @MockBean
    private VisitRepository visitRepository;
    @MockBean
    private DoctorRepository doctorRepository;
    @MockBean
    private PatientRepository patientRepository;
    @MockBean
    private DiagnosisRepository diagnosisRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test get all visits")
    void testGetAllVisits() throws Exception {
        VisitInfoDto first = new VisitInfoDto(3L, 4L, 8L, LocalDateTime.of(LocalDate.of(2023, 2, 5), LocalTime.of(12, 15, 19)),
                Set.of("healthy"), new BigDecimal("2.90"), true);
        VisitInfoDto second = new VisitInfoDto(6L, 2L, 13L, LocalDateTime.of(LocalDate.of(2022, 12, 15), LocalTime.of(9, 12, 49)),
                Set.of("broken leg", "broken arm"), new BigDecimal("2.90"), false);

        when(visitService.getAllVisits()).thenReturn(List.of(first, second));

        mockMvc.perform(get("/visits"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.size()").value(equalTo(2))))
                .andExpect((jsonPath("$[0].id").value(equalTo(3))))
                .andExpect((jsonPath("$[0].patientId").value(equalTo(4))))
                .andExpect((jsonPath("$[0].doctorId").value(equalTo(8))))
                .andExpect((jsonPath("$[0].dateTime").value(equalTo("05.02.2023 12:15:19"))))
                .andExpect((jsonPath("$[0].diagnoses", containsInAnyOrder("healthy"))))
                .andExpect((jsonPath("$[0].feePrice").value(equalTo(2.90))))
                .andExpect((jsonPath("$[0].paidByPatient").value(equalTo(true))))
                .andExpect((jsonPath("$[1].id").value(equalTo(6))))
                .andExpect((jsonPath("$[1].patientId").value(equalTo(2))))
                .andExpect((jsonPath("$[1].doctorId").value(equalTo(13))))
                .andExpect((jsonPath("$[1].dateTime").value(equalTo("15.12.2022 09:12:49"))))
                .andExpect((jsonPath("$[1].diagnoses", containsInAnyOrder("broken leg", "broken arm"))))
                .andExpect((jsonPath("$[1].feePrice").value(equalTo(2.90))))
                .andExpect((jsonPath("$[1].paidByPatient").value(equalTo(false))));
    }

    @Test
    @DisplayName("Test get visit info with valid id")
    void testGetVisitInfoWithValidId() throws Exception {
        when(visitRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(mock(VisitEntity.class)));

        VisitInfoDto infoDto = new VisitInfoDto(6L, 2L, 13L, LocalDateTime.of(LocalDate.of(2022, 12, 15), LocalTime.of(9, 12, 49)),
                Set.of("broken leg", "broken arm"), new BigDecimal("2.90"), false);
        when(visitService.getVisitInfo(anyLong())).thenReturn(infoDto);

        mockMvc.perform(get("/visits/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id").value(equalTo(6))))
                .andExpect((jsonPath("$.patientId").value(equalTo(2))))
                .andExpect((jsonPath("$.doctorId").value(equalTo(13))))
                .andExpect((jsonPath("$.dateTime").value(equalTo("15.12.2022 09:12:49"))))
                .andExpect((jsonPath("$.diagnoses", containsInAnyOrder("broken leg", "broken arm"))))
                .andExpect((jsonPath("$.feePrice").value(equalTo(2.90))))
                .andExpect((jsonPath("$.paidByPatient").value(equalTo(false))));
    }

    @Test
    @DisplayName("Test get visit info with invalid id")
    void testGetVisitInfoWithInvalidId() throws Exception {
        when(visitRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get("/visits/{id}", 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test add visit")
    void testAddVisit() throws Exception {
        when(doctorRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(mock(DoctorEntity.class)));
        when(patientRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(mock(PatientEntity.class)));
        when(diagnosisRepository.findById(anyLong())).thenReturn(Optional.of(mock(DiagnosisEntity.class)));

        AddVisitDto addVisitDto = new AddVisitDto(1L, 5L, Set.of(18L, 9L));
        VisitInfoDto visitInfoDto = new VisitInfoDto(3L, 4L, 8L, LocalDateTime.of(LocalDate.of(2023, 2, 5), LocalTime.of(12, 15, 19)),
                Set.of("healthy"), new BigDecimal("2.90"), true);
        when(visitService.addVisit(any(AddVisitDto.class))).thenReturn(visitInfoDto);

        mockMvc.perform(post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addVisitDto)))
                .andExpect(status().isCreated())
                .andExpect((jsonPath("$.id").value(equalTo(3))))
                .andExpect((jsonPath("$.patientId").value(equalTo(4))))
                .andExpect((jsonPath("$.doctorId").value(equalTo(8))))
                .andExpect((jsonPath("$.dateTime").value(equalTo("05.02.2023 12:15:19"))))
                .andExpect((jsonPath("$.diagnoses", containsInAnyOrder("healthy"))))
                .andExpect((jsonPath("$.feePrice").value(equalTo(2.90))))
                .andExpect((jsonPath("$.paidByPatient").value(equalTo(true))));
    }

    @Test
    @DisplayName("Test update visit with valid id")
    void testUpdateVisitWithValidId() throws Exception {
        when(doctorRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(mock(DoctorEntity.class)));
        when(patientRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(mock(PatientEntity.class)));
        when(diagnosisRepository.findById(anyLong())).thenReturn(Optional.of(mock(DiagnosisEntity.class)));
        when(visitRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(mock(VisitEntity.class)));

        UpdateVisitDto updateVisitDto = new UpdateVisitDto(2L, 5L, 10L, Set.of(3L, 13L, 18L));
        VisitInfoDto visitInfoDto = new VisitInfoDto(3L, 4L, 8L, LocalDateTime.of(LocalDate.of(2023, 2, 5), LocalTime.of(12, 15, 19)),
                Set.of("healthy"), new BigDecimal("2.90"), true);
        when(visitService.updateVisit(any(UpdateVisitDto.class))).thenReturn(visitInfoDto);

        mockMvc.perform(put("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateVisitDto)))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id").value(equalTo(3))))
                .andExpect((jsonPath("$.patientId").value(equalTo(4))))
                .andExpect((jsonPath("$.doctorId").value(equalTo(8))))
                .andExpect((jsonPath("$.dateTime").value(equalTo("05.02.2023 12:15:19"))))
                .andExpect((jsonPath("$.diagnoses", containsInAnyOrder("healthy"))))
                .andExpect((jsonPath("$.feePrice").value(equalTo(2.90))))
                .andExpect((jsonPath("$.paidByPatient").value(equalTo(true))));
    }

    @Test
    @DisplayName("Test update visit with invalid id")
    void testUpdateVisitWithInvalidId() throws Exception {
        when(visitRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());
        UpdateVisitDto updateVisitDto = new UpdateVisitDto(2L, 5L, 10L, Set.of(3L, 13L, 18L));

        mockMvc.perform(put("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateVisitDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test delete with valid id")
    void testDeleteWithValidId() throws Exception {
        when(visitRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(mock(VisitEntity.class)));

        mockMvc.perform(delete("/visits/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.message").value(equalTo("Visit with the given ID deleted"))));
    }

    @Test
    @DisplayName("Test delete with invalid id")
    void testDeleteWithInvalidId() throws Exception {
        when(visitRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/visits/{id}", 4L))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test get all visits income")
    void testGetAllVisitsIncome() throws Exception {
        when(visitService.getTotalVisitsIncome()).thenReturn(new BigDecimal("223.60"));
        mockMvc.perform(get("/visits/all-visits-income"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$").value(equalTo(223.60))));
    }

    @Test
    @DisplayName("Test get visits income by valid doctor")
    void testGetVisitsIncomeByValidDoctor() throws Exception {
        when(visitService.getVisitsIncomeByDoctorId(anyLong())).thenReturn(new BigDecimal("143.59"));
        mockMvc.perform(get("/visits/income-by-doctor")
                        .param("doctorId", "4"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$").value(equalTo(143.59))));
    }

    @Test
    @DisplayName("Test get visits income by invalid doctor")
    void testGetVisitsIncomeByInvalidDoctor() throws Exception {
        when(visitService.getVisitsIncomeByDoctorId(anyLong())).thenThrow(new DoctorNotFoundException(4L));
        mockMvc.perform(get("/visits/income-by-doctor")
                        .param("doctorId", "4"))
                .andExpect(status().isNotFound())
                .andExpect((jsonPath("$.message").value(equalTo("Doctor with ID 4 not found"))));
    }

    @Test
    @DisplayName("Test get visits count by valid patient")
    void testGetVisitsCountByValidPatient() throws Exception {
        when(visitService.countAllByPatientId(anyLong())).thenReturn(19L);
        mockMvc.perform(get("/visits/count-by-patient")
                        .param("patientId", "3"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$").value(equalTo(19))));
    }

    @Test
    @DisplayName("Test get visits count by invalid patient")
    void testGetVisitsCountByInvalidPatient() throws Exception {
        when(visitService.countAllByPatientId(anyLong())).thenThrow(new PatientNotFoundException(7L));
        mockMvc.perform(get("/visits/count-by-patient")
                        .param("patientId", "7"))
                .andExpect(status().isNotFound())
                .andExpect((jsonPath("$.message").value(equalTo("Patient with ID 7 not found"))));
    }

    @Test
    @DisplayName("Test get visits count by valid diagnosis")
    void testGetVisitsCountByValidDiagnosis() throws Exception {
        when(visitService.countAllByContainingDiagnosisId(anyLong())).thenReturn(86L);
        mockMvc.perform(get("/visits/count-by-diagnosis")
                        .param("diagnosisId", "3"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$").value(equalTo(86))));
    }

    @Test
    @DisplayName("Test get visits count by invalid diagnosis")
    void testGetVisitsCountByInvalidDiagnosis() throws Exception {
        when(visitService.countAllByContainingDiagnosisId(anyLong())).thenThrow(new DiagnosisNotFoundException(17L));
        mockMvc.perform(get("/visits/count-by-diagnosis")
                        .param("diagnosisId", "17"))
                .andExpect(status().isNotFound())
                .andExpect((jsonPath("$.message").value(equalTo("Diagnosis with ID 17 not found"))));
    }

    @Test
    @DisplayName("Test get visits income by valid diagnosis")
    void testGetVisitsIncomeByValidDiagnosis() throws Exception {
        when(visitService.getVisitsIncomeByDiagnosisId(anyLong())).thenReturn(new BigDecimal("538.94"));
        mockMvc.perform(get("/visits/income-by-diagnosis")
                        .param("diagnosisId", "4"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$").value(equalTo(538.94))));
    }

    @Test
    @DisplayName("Test get visits income by invalid diagnosis")
    void testGetVisitsIncomeByInvalidDiagnosis() throws Exception {
        when(visitService.getVisitsIncomeByDiagnosisId(anyLong())).thenThrow(new DiagnosisNotFoundException(14L));
        mockMvc.perform(get("/visits/income-by-diagnosis")
                        .param("diagnosisId", "14"))
                .andExpect(status().isNotFound())
                .andExpect((jsonPath("$.message").value(equalTo("Diagnosis with ID 14 not found"))));
    }

    @Test
    @DisplayName("Test get visits income by non-insured patients")
    void testGetVisitsIncomeByNonInsuredPatients() throws Exception {
        when(visitService.getVisitsIncomeByNonInsuredPatients()).thenReturn(new BigDecimal("89.53"));
        mockMvc.perform(get("/visits/income-by-non-insured-patients"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$").value(equalTo(89.53))));
    }
}
package com.rewe.medical_record.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rewe.medical_record.data.dto.diagnosis.AddDiagnosisDto;
import com.rewe.medical_record.data.dto.diagnosis.DiagnosisInfoDto;
import com.rewe.medical_record.data.dto.diagnosis.UpdateDiagnosisDto;
import com.rewe.medical_record.data.entity.DiagnosisEntity;
import com.rewe.medical_record.data.repository.DiagnosisRepository;
import com.rewe.medical_record.service.DiagnosisService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DiagnosisRestController.class)
@AutoConfigureMockMvc
class DiagnosisRestControllerTest {
    @MockBean
    private DiagnosisService diagnosisService;
    @MockBean
    private DiagnosisRepository diagnosisRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test get all diagnoses")
    void testGetAllDiagnoses() throws Exception {
        DiagnosisInfoDto infoDto1 = new DiagnosisInfoDto(1L, "flu", "flu");
        DiagnosisInfoDto infoDto2 = new DiagnosisInfoDto(2L, "asthma", "asthma");
        when(diagnosisService.getAllDiagnoses()).thenReturn(List.of(infoDto1, infoDto2));

        mockMvc.perform(get("/diagnoses"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.size()").value(equalTo(2))))
                .andExpect((jsonPath("$[0].id").value(equalTo(1))))
                .andExpect((jsonPath("$[0].name").value(equalTo("flu"))))
                .andExpect((jsonPath("$[0].description").value(equalTo("flu"))))
                .andExpect((jsonPath("$[1].id").value(equalTo(2))))
                .andExpect((jsonPath("$[1].name").value(equalTo("asthma"))))
                .andExpect((jsonPath("$[1].description").value(equalTo("asthma"))));
    }

    @Test
    @DisplayName("Test get diagnosis with valid id")
    void testGetDiagnosis() throws Exception {
        DiagnosisInfoDto infoDto = new DiagnosisInfoDto(2L, "asthma", "asthma");
        when(diagnosisService.getDiagnosis(anyLong())).thenReturn(infoDto);
        when(diagnosisRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(mock(DiagnosisEntity.class)));

        mockMvc.perform(get("/diagnoses/{id}", 1))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id").value(equalTo(2))))
                .andExpect((jsonPath("$.name").value(equalTo("asthma"))))
                .andExpect((jsonPath("$.description").value(equalTo("asthma"))));
    }

    @Test
    @DisplayName("Test get diagnosis with invalid id")
    void testGetDiagnosisWithInvalidId() throws Exception {
        when(diagnosisRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get("/diagnoses/{id}", 1))
                .andExpect(status().isBadRequest())
                .andExpect((jsonPath("$.message", Matchers.contains("No diagnosis with the given ID found"))));
    }

    @Test
    @DisplayName("Test create diagnosis")
    void testCreateDiagnosis() throws Exception {
        AddDiagnosisDto addDiagnosisDto = new AddDiagnosisDto("flu", "flu");
        DiagnosisInfoDto infoDto = new DiagnosisInfoDto(1L, "flu", "flu");
        when(diagnosisService.createDiagnosis(any(AddDiagnosisDto.class))).thenReturn(infoDto);
        mockMvc.perform(post("/diagnoses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addDiagnosisDto)))
                .andExpect(status().isCreated())
                .andExpect((jsonPath("$.id").value(equalTo(1))))
                .andExpect((jsonPath("$.name").value(equalTo("flu"))))
                .andExpect((jsonPath("$.description").value(equalTo("flu"))));
    }

    @Test
    @DisplayName("Test update diagnosis with valid id")
    void testUpdateDiagnosisWithValidId() throws Exception {
        UpdateDiagnosisDto updateDiagnosisDto = new UpdateDiagnosisDto(2L, "asthma", "asthma");
        DiagnosisInfoDto infoDto = new DiagnosisInfoDto(2L, "asthma", "asthma");
        when(diagnosisService.updateDiagnosis(any(UpdateDiagnosisDto.class))).thenReturn(infoDto);
        when(diagnosisRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(mock(DiagnosisEntity.class)));

        mockMvc.perform(put("/diagnoses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDiagnosisDto)))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id").value(equalTo(2))))
                .andExpect((jsonPath("$.name").value(equalTo("asthma"))))
                .andExpect((jsonPath("$.description").value(equalTo("asthma"))));
    }

    @Test
    @DisplayName("Test update diagnosis with invalid id")
    void testUpdateDiagnosisWithInvalidId() throws Exception {
        UpdateDiagnosisDto updateDiagnosisDto = new UpdateDiagnosisDto(2L, "asthma", "asthma");
        when(diagnosisRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(put("/diagnoses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDiagnosisDto)))
                .andExpect(status().isBadRequest())
                .andExpect((jsonPath("$.message", Matchers.contains("No diagnosis with the given ID found"))));
    }

    @Test
    @DisplayName("Test delete diagnosis with valid id")
    void testDeleteDiagnosisWithValidId() throws Exception {
        when(diagnosisRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(mock(DiagnosisEntity.class)));

        mockMvc.perform(delete("/diagnoses/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.message").value(equalTo("Diagnosis with the given ID deleted"))));
    }

    @Test
    @DisplayName("Test delete diagnosis with invalid id")
    void testDeleteDiagnosisWithInvalidId() throws Exception {
        when(diagnosisRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/diagnoses/{id}", 1))
                .andExpect(status().isBadRequest())
                .andExpect((jsonPath("$.message", Matchers.contains("No diagnosis with the given ID found"))));
    }
}
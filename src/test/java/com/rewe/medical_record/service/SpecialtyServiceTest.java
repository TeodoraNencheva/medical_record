package com.rewe.medical_record.service;

import com.rewe.medical_record.data.dto.specialty.AddSpecialtyDto;
import com.rewe.medical_record.data.dto.specialty.SpecialtyInfoDto;
import com.rewe.medical_record.data.dto.specialty.UpdateSpecialtyDto;
import com.rewe.medical_record.data.entity.SpecialtyEntity;
import com.rewe.medical_record.data.repository.SpecialtyRepository;
import com.rewe.medical_record.exceptions.SpecialtyNotFoundException;
import com.rewe.medical_record.mapper.SpecialtyMapper;
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
class SpecialtyServiceTest {
    @Mock
    private SpecialtyRepository specialtyRepository;
    @Mock
    private SpecialtyMapper specialtyMapper;
    @InjectMocks
    private SpecialtyService specialtyService;
    private SpecialtyEntity first, second;

    @BeforeEach
    void setUp() {
        first = new SpecialtyEntity("Cardiology", "Cardiology", false);
        ReflectionTestUtils.setField(first, "id", 1L);
        second = new SpecialtyEntity("Dermatology", "Dermatology", false);
        ReflectionTestUtils.setField(second, "id", 2L);
    }

    @Test
    @DisplayName("Test get all specialties")
    void getAllSpecialtiesTest() {
        when(specialtyRepository.findAllByDeletedFalse()).thenReturn(List.of(first, second));

        SpecialtyInfoDto infoDto1 = new SpecialtyInfoDto(1L, "Cardiology", "Cardiology");
        SpecialtyInfoDto infoDto2 = new SpecialtyInfoDto(2L, "Dermatology", "Dermatology");
        when(specialtyMapper.specialtyEntityToSpecialtyInfoDto(first)).thenReturn(infoDto1);
        when(specialtyMapper.specialtyEntityToSpecialtyInfoDto(second)).thenReturn(infoDto2);

        assertIterableEquals(List.of(infoDto1, infoDto2), specialtyService.getAllSpecialties());
    }

    @Test
    @DisplayName("Test get specialty with valid id")
    void testGetSpecialtyWithValidId() {
        when(specialtyRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(second));
        SpecialtyInfoDto infoDto = new SpecialtyInfoDto(2L, "Dermatology", "Dermatology");
        when(specialtyMapper.specialtyEntityToSpecialtyInfoDto(second)).thenReturn(infoDto);

        assertEquals(infoDto, specialtyService.getSpecialty(2L));
    }

    @Test
    @DisplayName("Test get specialty with invalid id")
    void testGetSpecialtyWithInvalidId() {
        when(specialtyRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());
        assertThrows(SpecialtyNotFoundException.class, () -> specialtyService.getSpecialty(1L));
    }

    @Test
    @DisplayName("Test create specialty")
    void testCreateSpecialty() {
        AddSpecialtyDto addDto = new AddSpecialtyDto("Cardiology", "Cardiology");
        when(specialtyMapper.addSpecialtyDtoToSpecialtyEntity(any(AddSpecialtyDto.class))).thenReturn(first);
        when(specialtyRepository.save(first)).thenReturn(first);
        SpecialtyInfoDto infoDto = new SpecialtyInfoDto(1L, "Cardiology", "Cardiology");
        when(specialtyMapper.specialtyEntityToSpecialtyInfoDto(first)).thenReturn(infoDto);
        assertEquals(infoDto, specialtyService.createSpecialty(addDto));
    }

    @Test
    @DisplayName("Test update specialty with valid id")
    void testUpdateSpecialtyWithValidId() {
        UpdateSpecialtyDto updateDto = new UpdateSpecialtyDto(1L, "Cardiology", "Cardiology");
        when(specialtyRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(second));
        when(specialtyMapper.updateSpecialtyDtoToSpecialtyEntity(any(UpdateSpecialtyDto.class))).thenReturn(first);
        when(specialtyRepository.save(any(SpecialtyEntity.class))).thenReturn(second);
        SpecialtyInfoDto infoDto = new SpecialtyInfoDto(1L, "Cardiology", "Cardiology");
        when(specialtyMapper.specialtyEntityToSpecialtyInfoDto(any(SpecialtyEntity.class))).thenReturn(infoDto);

        assertEquals(infoDto, specialtyService.updateSpecialty(updateDto));
        assertEquals(first.getName(), second.getName());
        assertEquals(first.getDescription(), second.getDescription());
    }

    @Test
    @DisplayName("Test update specialty with invalid id")
    void testUpdateSpecialtyWithInvalidId() {
        UpdateSpecialtyDto updateDto = new UpdateSpecialtyDto(1L, "Cardiology", "Cardiology");
        when(specialtyRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());
        assertThrows(SpecialtyNotFoundException.class, () -> specialtyService.updateSpecialty(updateDto));
    }

    @Test
    @DisplayName("Test delete specialty with valid id")
    void testDeleteSpecialtyWithValidId() {
        when(specialtyRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(second));
        specialtyService.deleteSpecialty(1L);
        assertTrue(second.isDeleted());
        verify(specialtyRepository).save(second);
    }

    @Test
    @DisplayName("Test delete specialty with invalid id")
    void testDeleteSpecialtyWithInvalidId() {
        when(specialtyRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());
        assertThrows(SpecialtyNotFoundException.class, () -> specialtyService.deleteSpecialty(1L));
        verify(specialtyRepository, never()).save(any(SpecialtyEntity.class));
    }
}
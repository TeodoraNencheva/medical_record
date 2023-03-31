package com.rewe.medical_record.service;

import com.rewe.medical_record.data.dto.specialty.AddSpecialtyDto;
import com.rewe.medical_record.data.dto.specialty.SpecialtyInfoDto;
import com.rewe.medical_record.data.dto.specialty.UpdateSpecialtyDto;
import com.rewe.medical_record.data.entity.SpecialtyEntity;
import com.rewe.medical_record.data.repository.SpecialtyRepository;
import com.rewe.medical_record.exceptions.SpecialtyNotFoundException;
import com.rewe.medical_record.mapper.SpecialtyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialtyService {
    private final SpecialtyRepository specialtyRepository;
    private final SpecialtyMapper specialtyMapper;

    public List<SpecialtyInfoDto> getAllSpecialties() {
        return specialtyRepository.findAllByDeletedFalse()
                .stream()
                .map(specialtyMapper::specialtyEntityToSpecialtyInfoDto)
                .toList();
    }

    public SpecialtyInfoDto getSpecialty(Long id) {
        SpecialtyEntity specialty = specialtyRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new SpecialtyNotFoundException(id));
        return specialtyMapper.specialtyEntityToSpecialtyInfoDto(specialty);
    }

    public SpecialtyInfoDto createSpecialty(AddSpecialtyDto specialtyDto) {
        SpecialtyEntity saved = specialtyRepository.save(specialtyMapper.addSpecialtyDtoToSpecialtyEntity(specialtyDto));
        return specialtyMapper.specialtyEntityToSpecialtyInfoDto(saved);
    }

    public SpecialtyInfoDto updateSpecialty(UpdateSpecialtyDto specialtyDto) {
        SpecialtyEntity specialty = specialtyRepository
                .findByIdAndDeletedFalse(specialtyDto.getId())
                .orElseThrow(() -> new SpecialtyNotFoundException(specialtyDto.getId()));
        SpecialtyEntity newProperties = specialtyMapper.updateSpecialtyDtoToSpecialtyEntity(specialtyDto);
        specialty.setName(newProperties.getName());
        specialty.setDescription(newProperties.getDescription());
        SpecialtyEntity saved = specialtyRepository.save(specialty);
        return specialtyMapper.specialtyEntityToSpecialtyInfoDto(saved);
    }

    public void deleteSpecialty(Long id) {
        SpecialtyEntity specialty = specialtyRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new SpecialtyNotFoundException(id));
        specialty.setDeleted(true);
        specialtyRepository.save(specialty);
    }
}

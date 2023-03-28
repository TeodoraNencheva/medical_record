package com.rewe.medical_record.service;

import com.rewe.medical_record.data.repository.VisitRepository;
import com.rewe.medical_record.mapper.VisitMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;
}

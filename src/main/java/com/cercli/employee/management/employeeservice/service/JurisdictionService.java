package com.cercli.employee.management.employeeservice.service;

import com.cercli.employee.management.employeeservice.dto.JurisdictionDto;
import com.cercli.employee.management.employeeservice.entity.JurisdictionEntity;
import com.cercli.employee.management.employeeservice.mapper.JurisdictionMapper;
import com.cercli.employee.management.employeeservice.repository.JurisdictionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JurisdictionService {
    JurisdictionRepository repository;
    JurisdictionMapper mapper;

    public JurisdictionDto addJurisdiction(JurisdictionDto jurisdictionDto) {
        return mapper.mapToDto(repository.save(mapper.mapToEntity(jurisdictionDto)));
    }

    public Optional<JurisdictionEntity> getJurisdictionByCode(String code) {
        return repository.findById(code);
    }

    public List<JurisdictionEntity> getAllJurisdictions() {
        return repository.findAll();
    }

    public boolean isNotValidJurisdiction(String code) {
        return getJurisdictionByCode(code).isEmpty();
    }

    public void deleteJurisdiction(String code) {
        repository.deleteById(code);
    }
}

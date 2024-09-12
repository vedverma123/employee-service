package com.cercli.employee.management.employeeservice.service;

import com.cercli.employee.management.employeeservice.EmployeeException;
import com.cercli.employee.management.employeeservice.dto.EmployeeDto;
import com.cercli.employee.management.employeeservice.entity.EmployeeEntity;
import com.cercli.employee.management.employeeservice.mapper.EmployeeMapper;
import com.cercli.employee.management.employeeservice.repository.EmployeeRepository;
import com.cercli.employee.management.employeeservice.util.TimezoneUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EmployeeService {

    EmployeeMapper employeeMapper;
    EmployeeRepository repository;
    JurisdictionService jurisdictionService;

    public EmployeeDto addEmployee(final EmployeeDto employeeDto) {
        if (jurisdictionService.isNotValidJurisdiction(employeeDto.getJurisdictionCode())) {
            throw new IllegalArgumentException("Jurisdiction not found: " + employeeDto.getJurisdictionCode());
        }
        EmployeeEntity employeeEntity = employeeMapper.mapToEntity(employeeDto);
        employeeEntity.setCreatedAt(ZonedDateTime.now());
        return employeeMapper.mapToDto(repository.save(employeeEntity));
    }

    public EmployeeDto getEmployee(final UUID employeeId, ZoneId employeeZoneId) {
        return repository.findById(employeeId)
                .map(employeeMapper::mapToDto)
                .map(employeeDto -> this.convertToLocalTime(employeeDto, employeeZoneId))
                .orElseThrow(() -> new EmployeeException("Unable to find employee for id : " + employeeId));
    }

    private EmployeeDto convertToLocalTime(final EmployeeDto employeeDto, ZoneId employeeZoneId) {
        employeeDto.setCreatedAt(TimezoneUtil.convertToLocalTime(employeeDto.getCreatedAt(), employeeZoneId));
        employeeDto.setModifiedAt(TimezoneUtil.convertToLocalTime(employeeDto.getModifiedAt(), employeeZoneId));
        return employeeDto;
    }

    public List<EmployeeDto> getAllEmployees(final ZoneId employeeZoneId) {
        return repository.findAll()
                .stream()
                .map(employeeMapper::mapToDto)
                .map(employeeDto -> this.convertToLocalTime(employeeDto, employeeZoneId))
                .toList();

    }

    public EmployeeDto updateEmployee(final UUID id, final EmployeeDto employeeDto) {
        Optional<EmployeeEntity> employeeOpt = repository.findById(id);
        if (employeeOpt.isEmpty()) {
            throw new EmployeeException("Employee not present for id : " + id);
        }
        if (jurisdictionService.isNotValidJurisdiction(employeeDto.getJurisdictionCode())) {
            throw new IllegalArgumentException("Jurisdiction not found: " + employeeDto.getJurisdictionCode());
        }

        EmployeeEntity newEmployee = employeeMapper.mapToEntity(employeeDto);
        EmployeeEntity existingEmployee = employeeOpt.get();
        newEmployee.setEmployeeId(existingEmployee.getEmployeeId());
        newEmployee.setModifiedAt(ZonedDateTime.now());
        newEmployee.setCreatedAt(existingEmployee.getCreatedAt());
        return employeeMapper.mapToDto(repository.save(newEmployee));
    }

    public void deleteEmployee(UUID id) {
        repository.deleteById(id);
    }
}

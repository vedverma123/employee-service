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

import static com.cercli.employee.management.employeeservice.constants.AppConstants.ZONE_UTC;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EmployeeService {

    EmployeeMapper employeeMapper;
    EmployeeRepository repository;
    JurisdictionService jurisdictionService;

    public EmployeeDto addEmployee(final EmployeeDto employeeDto) {
        // Validate jurisdiction code
        if (jurisdictionService.isNotValidJurisdiction(employeeDto.getJurisdictionCode())) {
            throw new IllegalArgumentException("Jurisdiction not found: " + employeeDto.getJurisdictionCode());
        }

        // Map EmployeeDto to EmployeeEntity
        // Assuming employeeDto contains a valid time zone
        EmployeeEntity employeeEntity = employeeMapper.mapToEntity(employeeDto);

        // Handle employee time zone
        ZoneId employeeZoneId = ZoneId.of(employeeEntity.getJurisdiction().getZoneId());

        // Convert the current time in the employee's time zone to UTC for storage
        ZonedDateTime employeeLocalTime = ZonedDateTime.now(employeeZoneId);
        ZonedDateTime utcTime = employeeLocalTime.withZoneSameInstant(ZoneId.of(ZONE_UTC));  // Convert to UTC

        employeeEntity.setCreatedAt(utcTime);  // Store the UTC time

        // Save and map the saved entity to DTO
        return employeeMapper.mapToDto(repository.save(employeeEntity));
    }

    public EmployeeDto getEmployee(final UUID employeeId, ZoneId employeeZoneId) {
        return repository.findById(employeeId)
                .map(employeeMapper::mapToDto)
                .map(employeeDto -> this.convertToLocalTime(employeeDto, employeeZoneId))
                .orElseThrow(() -> new EmployeeException("Unable to find employee for id : " + employeeId));
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

        EmployeeEntity existingEmployee = employeeOpt.get();

        // Map DTO to Entity
        EmployeeEntity newEmployee = employeeMapper.mapToEntity(employeeDto);
        newEmployee.setEmployeeId(existingEmployee.getEmployeeId());

        // Handle time zone for 'modifiedAt'
        ZoneId employeeZoneId = ZoneId.of(newEmployee.getJurisdiction().getZoneId());  // Assuming employee's time zone is in the DTO
        ZonedDateTime employeeLocalTime = ZonedDateTime.now(employeeZoneId);
        ZonedDateTime utcTime = employeeLocalTime.withZoneSameInstant(ZoneId.of(ZONE_UTC));

        newEmployee.setModifiedAt(utcTime);  // Store the 'modifiedAt' in UTC

        // Keep the original 'createdAt' time
        newEmployee.setCreatedAt(existingEmployee.getCreatedAt());

        // Save the updated entity
        return employeeMapper.mapToDto(repository.save(newEmployee));
    }


    public void deleteEmployee(UUID id) {
        repository.deleteById(id);
    }

    public EmployeeDto convertToLocalTime(final EmployeeDto employeeDto, ZoneId employeeZoneId) {
        employeeDto.setCreatedAt(TimezoneUtil.convertToLocalTime(employeeDto.getCreatedAt(), employeeZoneId));
        employeeDto.setModifiedAt(TimezoneUtil.convertToLocalTime(employeeDto.getModifiedAt(), employeeZoneId));
        return employeeDto;
    }
}

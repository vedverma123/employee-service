package com.cercli.employee.management.employeeservice.service;

import com.cercli.employee.management.employeeservice.EmployeeException;
import com.cercli.employee.management.employeeservice.dto.EmployeeDto;
import com.cercli.employee.management.employeeservice.entity.EmployeeEntity;
import com.cercli.employee.management.employeeservice.entity.JurisdictionEntity;
import com.cercli.employee.management.employeeservice.mapper.EmployeeMapper;
import com.cercli.employee.management.employeeservice.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private EmployeeRepository repository;

    @Mock
    private JurisdictionService jurisdictionService;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeDto employeeDto;
    private EmployeeEntity employeeEntity;
    private JurisdictionEntity jurisdictionEntity;
    private UUID employeeId;
    private ZoneId employeeZoneId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ZonedDateTime utc = ZonedDateTime.now(ZoneId.of("UTC"));
        employeeId = UUID.randomUUID();
        employeeDto = new EmployeeDto();
        employeeEntity = new EmployeeEntity();
        employeeEntity.setCreatedAt(utc);
        employeeEntity.setModifiedAt(utc);
        employeeDto.setCreatedAt(utc);
        employeeDto.setModifiedAt(utc);
        employeeZoneId = ZoneId.of("Asia/Kolkata");

        // Setup JurisdictionEntity
        jurisdictionEntity = new JurisdictionEntity();
        jurisdictionEntity.setCode("IN");
        jurisdictionEntity.setName("India");
        jurisdictionEntity.setZoneId("Asia/Kolkata");
        jurisdictionEntity.setLocale("en-IN");

        employeeEntity.setJurisdiction(jurisdictionEntity);
    }

    @Test
    void testAddEmployee_success() {
        // Mocking
        when(jurisdictionService.isNotValidJurisdiction(anyString())).thenReturn(false);
        when(employeeMapper.mapToEntity(any(EmployeeDto.class))).thenReturn(employeeEntity);
        when(repository.save(any(EmployeeEntity.class))).thenReturn(employeeEntity);
        when(employeeMapper.mapToDto(any(EmployeeEntity.class))).thenReturn(employeeDto);

        // Execution
        EmployeeDto result = employeeService.addEmployee(employeeDto);

        // Verification
        assertNotNull(result);
        verify(jurisdictionService).isNotValidJurisdiction(null);
        verify(repository).save(any(EmployeeEntity.class));
        verify(employeeMapper).mapToEntity(any(EmployeeDto.class));
        verify(employeeMapper).mapToDto(any(EmployeeEntity.class));
    }

    @Test
    void testAddEmployee_invalidJurisdiction() {
        employeeDto.setJurisdictionCode("IN");
        // Mocking
        when(jurisdictionService.isNotValidJurisdiction(anyString())).thenReturn(true);

        // Execution & Assertion
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                                                         () -> employeeService.addEmployee(employeeDto));

        assertEquals("Jurisdiction not found: IN", exception.getMessage());
        verify(jurisdictionService).isNotValidJurisdiction(anyString());
        verifyNoInteractions(employeeMapper);
        verifyNoInteractions(repository);
    }

    @Test
    void testGetEmployee_success() {
        // Mocking
        when(repository.findById(employeeId)).thenReturn(Optional.of(employeeEntity));
        when(employeeMapper.mapToDto(any(EmployeeEntity.class))).thenReturn(employeeDto);

        // Execution
        EmployeeDto result = employeeService.getEmployee(employeeId, employeeZoneId);

        // Verification
        assertNotNull(result);
        verify(repository).findById(employeeId);
        verify(employeeMapper).mapToDto(any(EmployeeEntity.class));
    }

    @Test
    void testGetEmployee_employeeNotFound() {
        // Mocking
        when(repository.findById(employeeId)).thenReturn(Optional.empty());

        // Execution & Assertion
        EmployeeException exception = assertThrows(EmployeeException.class, () -> {
            employeeService.getEmployee(employeeId, employeeZoneId);
        });

        assertEquals("Unable to find employee for id : " + employeeId, exception.getMessage());
        verify(repository).findById(employeeId);
        verifyNoInteractions(employeeMapper);
    }

    @Test
    void testGetAllEmployees_success() {
        // Mocking
        List<EmployeeEntity> employeeEntities = List.of(employeeEntity);
        when(repository.findAll()).thenReturn(employeeEntities);
        when(employeeMapper.mapToDto(any(EmployeeEntity.class))).thenReturn(employeeDto);

        // Execution
        List<EmployeeDto> result = employeeService.getAllEmployees(employeeZoneId);

        // Verification
        assertFalse(result.isEmpty());
        verify(repository).findAll();
        verify(employeeMapper, times(employeeEntities.size())).mapToDto(any(EmployeeEntity.class));
    }

    @Test
    void testUpdateEmployee_success() {
        // Mocking
        when(repository.findById(employeeId)).thenReturn(Optional.of(employeeEntity));
        when(jurisdictionService.isNotValidJurisdiction(anyString())).thenReturn(false);
        when(employeeMapper.mapToEntity(any(EmployeeDto.class))).thenReturn(employeeEntity);
        when(repository.save(any(EmployeeEntity.class))).thenReturn(employeeEntity);
        when(employeeMapper.mapToDto(any(EmployeeEntity.class))).thenReturn(employeeDto);

        // Execution
        EmployeeDto result = employeeService.updateEmployee(employeeId, employeeDto);

        // Verification
        assertNotNull(result);
        verify(repository).findById(employeeId);
        verify(repository).save(any(EmployeeEntity.class));
        verify(employeeMapper).mapToDto(any(EmployeeEntity.class));
    }

    @Test
    void testUpdateEmployee_employeeNotFound() {
        // Mocking
        when(repository.findById(employeeId)).thenReturn(Optional.empty());

        // Execution & Assertion
        EmployeeException exception = assertThrows(EmployeeException.class, () -> {
            employeeService.updateEmployee(employeeId, employeeDto);
        });

        assertEquals("Employee not present for id : " + employeeId, exception.getMessage());
        verify(repository).findById(employeeId);
        verifyNoInteractions(employeeMapper);
    }

    @Test
    void testDeleteEmployee_success() {
        // Execution
        employeeService.deleteEmployee(employeeId);

        // Verification
        verify(repository).deleteById(employeeId);
    }
}

package com.cercli.employee.management.employeeservice.mapper;

import com.cercli.employee.management.employeeservice.dto.EmployeeDto;
import com.cercli.employee.management.employeeservice.entity.EmployeeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeEntity mapToEntity(EmployeeDto source);

    EmployeeDto mapToDto(EmployeeEntity source);
}

package com.cercli.employee.management.employeeservice.mapper;

import com.cercli.employee.management.employeeservice.dto.JurisdictionDto;
import com.cercli.employee.management.employeeservice.entity.JurisdictionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JurisdictionMapper {

    JurisdictionDto mapToDto(JurisdictionEntity source);

    JurisdictionEntity mapToEntity(JurisdictionDto source);
}

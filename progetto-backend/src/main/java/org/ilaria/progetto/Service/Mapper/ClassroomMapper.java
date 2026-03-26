package org.ilaria.progetto.Service.Mapper;

import org.ilaria.progetto.Model.DTO.ClassroomDTO;
import org.ilaria.progetto.Model.Entity.Classroom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClassroomMapper {

    @Mapping(target = "personInCharge", ignore = true)
    ClassroomDTO toDto(Classroom classroom);

    @Mapping(target = "personInCharge", ignore = true)
    @Mapping(target = "version", ignore = true)
    Classroom toEntity(ClassroomDTO classroomDTO);
}
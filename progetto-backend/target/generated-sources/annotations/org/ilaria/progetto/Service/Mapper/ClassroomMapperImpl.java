package org.ilaria.progetto.Service.Mapper;

import javax.annotation.processing.Generated;
import org.ilaria.progetto.Model.DTO.ClassroomDTO;
import org.ilaria.progetto.Model.Entity.Classroom;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-24T09:31:01+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class ClassroomMapperImpl implements ClassroomMapper {

    @Override
    public ClassroomDTO toDto(Classroom classroom) {
        if ( classroom == null ) {
            return null;
        }

        ClassroomDTO classroomDTO = new ClassroomDTO();

        if ( classroom.getId() != null ) {
            classroomDTO.setId( classroom.getId() );
        }
        classroomDTO.setCube( classroom.getCube() );
        classroomDTO.setNumberOfSeats( classroom.getNumberOfSeats() );
        classroomDTO.setTotalSeats( classroom.getTotalSeats() );
        classroomDTO.setFloor( classroom.getFloor() );
        classroomDTO.setLaboratory( classroom.isLaboratory() );

        return classroomDTO;
    }

    @Override
    public Classroom toEntity(ClassroomDTO classroomDTO) {
        if ( classroomDTO == null ) {
            return null;
        }

        Classroom classroom = new Classroom();

        classroom.setId( classroomDTO.getId() );
        classroom.setCube( classroomDTO.getCube() );
        classroom.setNumberOfSeats( classroomDTO.getNumberOfSeats() );
        classroom.setTotalSeats( classroomDTO.getTotalSeats() );
        classroom.setFloor( classroomDTO.getFloor() );
        classroom.setLaboratory( classroomDTO.isLaboratory() );

        return classroom;
    }
}

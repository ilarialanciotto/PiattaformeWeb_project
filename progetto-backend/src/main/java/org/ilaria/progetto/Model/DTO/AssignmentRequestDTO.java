package org.ilaria.progetto.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssignmentRequestDTO {

    private long classroomID;
    private long teacherID;

}
